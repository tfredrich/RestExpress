/*
 * Copyright 2009, Strategic Gains, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.strategicgains.restexpress;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.strategicgains.restexpress.exception.BadRequestException;
import com.strategicgains.restexpress.route.Route;
import com.strategicgains.restexpress.route.RouteResolver;
import com.strategicgains.restexpress.serialization.SerializationProcessor;

/**
 * @author toddf
 * @since Nov 20, 2009
 */
public class Request
{
	// SECTION: CONSTANTS

	private static final String METHOD_QUERY_PARAMETER = "_method";
	private static final String FORMAT_HEADER_NAME = "format";
	private static final String DEFAULT_PROTOCOL = "http";
	
	private static long nextCorrelationId = 0;


	// SECTION: INSTANCE VARIABLES

	private HttpRequest httpRequest;
	private SerializationProcessor serializationProcessor;
	private RouteResolver urlRouter;
	private HttpMethod realMethod;
	private Route resolvedRoute;
	private String correlationId;

	
	// SECTION: CONSTRUCTOR

	public Request(HttpRequest request, RouteResolver routes)
	{
		super();
		this.httpRequest = request;
		this.realMethod = request.getMethod();
		this.urlRouter = routes;
		parseRequestedFormatToHeader(request);
		handleMethodTunneling(addQueryStringParametersAsHeaders(request));
		createCorrelationId();
	}


	// SECTION: ACCESSORS/MUTATORS

	/**
	 * Return the Correlation ID for this request.  The Correlation ID is unique for each request within
	 * this VM instance.  Restarting the VM will reset the correlation ID to zero.  It is not a GUID.
	 * It is useful, however, in correlating events in the pipeline (e.g. timing, etc.).  
	 */
	public String getCorrelationId()
	{
		return correlationId;
	}

	/**
	 * Return the HTTP method of the request.
	 * 
	 * @return HttpMethod of the request.
	 */
	public HttpMethod getMethod()
	{
		return httpRequest.getMethod();
	}
	
	/**
	 * Return the requested HTTP method of the request, whether via the request's HTTP method
	 * or via a query parameter (e.g. "_Method=").
	 * 
	 * @return the requested HttpMethod.
	 */
	public HttpMethod getRealMethod()
	{
		return realMethod;
	}

	public boolean isMethodGet()
	{
		return getRealMethod().equals(HttpMethod.GET);
	}

	public boolean isMethodDelete()
	{
		return getRealMethod().equals(HttpMethod.DELETE);
	}

	public boolean isMethodPost()
	{
		return getRealMethod().equals(HttpMethod.POST);
	}

	public boolean isMethodPut()
	{
		return getRealMethod().equals(HttpMethod.PUT);
	}

	public ChannelBuffer getBody()
    {
		return httpRequest.getContent();
    }
	
	/**
	 * Attempts to deserialize the request body into an instance of the given type.
	 * 
	 * @param type the resulting type
	 * @return an instance of the requested type.
	 * @throws BadRequestException if the deserialization fails.
	 */
	public <T> T getBodyAs(Class<T> type)
	{
		try
		{
			return serializationProcessor.deserialize(getBody(), type);
		}
		catch(Exception e)
		{
			throw new BadRequestException(e);
		}
	}

	/**
	 * Attempts to deserialize the request body into an instance of the given type.
	 * If the serialization process returns null, throws BadResquestExcption using
	 * the message.
	 * 
	 * @param type the resulting type.
	 * @param message the message for the BadRequestException if serialization returns null.
	 * @return an instance of the requested type.
	 * @throws BadRequestException if serialization fails.
	 */
	public <T> T getBodyAs(Class<T> type, String message)
	{
		T instance = getBodyAs(type);

		if (instance == null)
		{
			throw new BadRequestException(message);
		}
		
		return instance;
	}

	public SerializationProcessor getSerializationProcessor()
    {
    	return serializationProcessor;
    }

	public void setSerializationProcessor(SerializationProcessor serializationProcessor)
    {
    	this.serializationProcessor = serializationProcessor;
    }

	public void setBody(ChannelBuffer body)
    {
		httpRequest.setContent(body);
    }

	public void clearHeaders()
	{
		httpRequest.clearHeaders();
	}

	public String getHeader(String name)
	{
		return httpRequest.getHeader(name);
	}

	public String getHeader(String name, String message)
	{
		String value = getHeader(name);
		
		if (value == null)
		{
			throw new BadRequestException(message);
		}

		return value;
	}
	
	public void addHeader(String name, String value)
    {
		httpRequest.addHeader(name, value);
    }
	
	public void addAllHeaders(Collection<Entry<String, String>> headers)
	{
    	for (Entry<String, String> entry : headers)
    	{
    		addHeader(entry.getKey(), entry.getValue());
    	}
	}

	public Route getResolvedRoute()
	{
		return resolvedRoute;
	}
	
	public void setResolvedRoute(Route route)
	{
		this.resolvedRoute = route;
	}

	/**
	 * Gets the path for this request.
	 * 
	 * @return
	 */
	public String getPath()
	{
		return httpRequest.getUri();
	}

	/**
	 * Returns the full URL for the request, containing protocol, host and path.
	 * 
	 * @return the full URL for the request.
	 */
	public String getUrl()
	{
		return getProtocol() + "://" + getHost() + getPath();
	}
	
	public String getNamedUrl(String resourceName)
	{
		Route route = urlRouter.getNamedRoute(resourceName, getMethod());
		
		if (route != null)
		{
			return route.getPattern();
		}
		
		return null;
	}

	public boolean isKeepAlive()
	{
		return HttpHeaders.isKeepAlive(httpRequest);
	}

	public boolean isChunked()
	{
		return httpRequest.isChunked();
	}
	
	/**
	 * Get the value of the {format} header in the request.
	 * 
	 * @return
	 */
	public String getFormat()
	{
		return getHeader(FORMAT_HEADER_NAME);
	}
	
	/**
	 * Get the host (and port) from the request.
	 * 
	 * @return
	 */
	public String getHost()
	{
		return HttpHeaders.getHost(httpRequest);
	}

	/**
	 * Get the protocol of the request.  RESTExpress currently only supports 'http'
	 * and will always return that value.
	 * 
	 * @return "http"
	 */
	public String getProtocol()
	{
		return DEFAULT_PROTOCOL;
	}
	
	/**
	 * Checks the format request parameter against the given format value.
	 * Ignores case.
	 * 
	 * @param format
	 * @return true if the given format matches (case insensitive) the request format parameter. Otherwise false.
	 */
	public boolean isFormatEqual(String format)
	{
		return isHeaderEqual(FORMAT_HEADER_NAME, format);
	}
	
	/**
	 * Checks the value of the given header against the given value.
	 * Ignores case.  If the header value or given value is null or has a trimmed length
	 * of zero, returns false.
	 * 
	 * @param name the name of a header to check.
	 * @param value the expected value.
	 * @return true if the header equals (ignoring case) to the given value.
	 */
	public boolean isHeaderEqual(String name, String value)
	{
		String header = getHeader(name);
		
		if (header == null || header.trim().length() == 0 || value == null || value.trim().length() == 0)
			return false;
		
		return header.trim().equalsIgnoreCase(value.trim());
	}

	/**
     * @param request
     */
    private void parseRequestedFormatToHeader(HttpRequest request)
    {
    	String uri = getUri(request);
		int queryDelimiterIndex = uri.indexOf('?');
		String path = (queryDelimiterIndex > 0 ? uri.substring(0, queryDelimiterIndex) : uri);
    	int formatDelimiterIndex = path.indexOf('.');
    	String format = (formatDelimiterIndex > 0 ? path.substring(formatDelimiterIndex + 1) : null);
    	
    	if (format != null)
    	{
    		request.addHeader(FORMAT_HEADER_NAME, format);
    	}
    }
	
	/**
	 * Add the query string parameters to the request as headers.
	 */
	private Map<String, String> addQueryStringParametersAsHeaders(HttpRequest request)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		String uri = getUri(request);
		int x = uri.indexOf('?');
		String queryString = (x >= 0 ? uri.substring(x + 1) : null);
		
		if (queryString != null)
		{
			String[] params = queryString.split("&");
			
			for (String pair : params)
			{
				String[] keyValue = pair.split("=");
				
				if (keyValue.length == 1)
				{
					request.addHeader(keyValue[0], "");
					parameters.put(keyValue[0], "");
				}
				else
				{
					request.addHeader(keyValue[0], keyValue[1]);
					parameters.put(keyValue[0], keyValue[1]);
				}
			}
		}
		
		return parameters;
	}

	private String getUri(HttpRequest request)
	{
        try
        {
	        return URLDecoder.decode(request.getUri(), ContentType.ENCODING);
        }
        catch (UnsupportedEncodingException e)
        {
        	// UGH!
        }
        
        return "";
	}
	/**
	 * If the request HTTP method is post, allow a query string parameter to determine
	 * the request HTTP method of the post (e.g. _method=DELETE or _method=PUT).  This
	 * supports DELETE and PUT from the browser.
	 * 
	 * @param parameters
	 */
	private void handleMethodTunneling(Map<String, String> parameters)
	{
		if (! HttpMethod.POST.equals(getMethod()))
			return;

		for (Entry<String, String> entry : parameters.entrySet())
		{
			if (METHOD_QUERY_PARAMETER.equalsIgnoreCase(entry.getKey()))
			{
				realMethod = HttpMethod.valueOf(entry.getValue().toUpperCase());
				break;
			}
		}
	}
	
	private void createCorrelationId()
	{
		this.correlationId = String.valueOf(++nextCorrelationId);
	}
}
