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

package com.strategicgains.restx;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.strategicgains.restx.route.Route;
import com.strategicgains.restx.route.RouteResolver;
import com.strategicgains.restx.serialization.SerializationProcessor;

/**
 * @author toddf
 * @since Nov 20, 2009
 */
public class Request
{
	// SECTION: CONSTANTS

	private static final String METHOD_QUERY_PARAMETER = "_method";
	private static final String FORMAT_HEADER_NAME = "format";


	// SECTION: INSTANCE VARIABLES

	private HttpRequest httpRequest;
	private Resolver<SerializationProcessor> serializationResolver;
	private RouteResolver urlRouter;
	private HttpMethod realMethod;

	
	// SECTION: CONSTRUCTOR

	Request(HttpRequest request, Resolver<SerializationProcessor> serializationResolver, RouteResolver routes)
	{
		super();
		this.httpRequest = request;
		this.realMethod = request.getMethod();
		this.serializationResolver = serializationResolver;
		this.urlRouter = routes;
		handleMethodTunneling(addQueryStringParametersAsHeaders());
	}
	
	// SECTION: ACCESSORS/MUTATORS
	
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

	public ChannelBuffer getBody()
    {
		return httpRequest.getContent();
    }
	
	public <T> T getBodyAs(Class<T> type)
	{
		SerializationProcessor processor = serializationResolver.resolve(this);
		return processor.deserialize(getBody(), type);
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
	
	public void addHeader(String name, String value)
    {
		httpRequest.addHeader(name, value);
    }
	
	public String getUrl()
	{
		return httpRequest.getUri();
	}
	
	public String getNamedUrl(String resourceName)
	{
		Route route = urlRouter.getNamedRoute(resourceName);
		
		if (route != null)
		{
			return route.getUrlPattern();
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
	
	public String getFormat()
	{
		return getHeader(FORMAT_HEADER_NAME);
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
	 * Add the query string parameters to the request as headers.
	 */
	private Map<String, String> addQueryStringParametersAsHeaders()
	{
		Map<String, String> parameters = new HashMap<String, String>();
		String uri = httpRequest.getUri();
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
					httpRequest.addHeader(keyValue[0], "");
					parameters.put(keyValue[0], "");
				}
				else
				{
					httpRequest.addHeader(keyValue[0], keyValue[1]);
					parameters.put(keyValue[0], keyValue[1]);
				}
			}
		}
		
		return parameters;
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
}
