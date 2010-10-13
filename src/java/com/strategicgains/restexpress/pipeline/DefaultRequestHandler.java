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
package com.strategicgains.restexpress.pipeline;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.exception.ExceptionMapping;
import com.strategicgains.restexpress.exception.ServiceException;
import com.strategicgains.restexpress.response.DefaultHttpResponseWriter;
import com.strategicgains.restexpress.response.ErrorHttpResponseWriter;
import com.strategicgains.restexpress.response.HttpResponseWriter;
import com.strategicgains.restexpress.route.Action;
import com.strategicgains.restexpress.route.RouteResolver;
import com.strategicgains.restexpress.serialization.SerializationProcessor;
import com.strategicgains.restexpress.util.Resolver;

/**
 * @author toddf
 * @since Nov 13, 2009
 */
@Sharable
public class DefaultRequestHandler
extends SimpleChannelUpstreamHandler
implements PreprocessorAware, PostprocessorAware
{
	// SECTION: CONSTANTS

	private static final String JSONP_CALLBACK = "jsonp";

	
	// SECTION: INSTANCE VARIABLES

	private RouteResolver routeResolver;
	private Resolver<SerializationProcessor> serializationResolver;
	private HttpResponseWriter responseWriter;
	private HttpResponseWriter errorResponseWriter;
	private List<Preprocessor> preprocessors = new ArrayList<Preprocessor>();
	private List<Postprocessor> postprocessors = new ArrayList<Postprocessor>();
	private ExceptionMapping exceptionMap = new ExceptionMapping();


	// SECTION: CONSTRUCTORS

	public DefaultRequestHandler(RouteResolver routeResolver, Resolver<SerializationProcessor> serializationResolver)
	{
		this(routeResolver, serializationResolver, new DefaultHttpResponseWriter(), new ErrorHttpResponseWriter());
	}

	public DefaultRequestHandler(RouteResolver routeResolver, Resolver<SerializationProcessor> serializationResolver,
		HttpResponseWriter responseWriter, HttpResponseWriter errorResponseWriter)
	{
		super();
		this.routeResolver = routeResolver;
		this.serializationResolver = serializationResolver;
		setResponseWriter(responseWriter);
		setErrorResponseWriter(errorResponseWriter);
	}


	// SECTION: MUTATORS
	
	public <T extends Exception, U extends ServiceException> DefaultRequestHandler mapException(Class<T> from, Class<U> to)
	{
		exceptionMap.map(from, to);
		return this;
	}

	public HttpResponseWriter getResponseWriter()
	{
		return this.responseWriter;
	}
	
	public HttpResponseWriter getErrorResponseWriter()
	{
		return this.errorResponseWriter;
	}

	public void setResponseWriter(HttpResponseWriter writer)
	{
		this.responseWriter = writer;
	}
	
	public void setErrorResponseWriter(HttpResponseWriter writer)
	{
		this.errorResponseWriter = writer;
	}


	// SECTION: SIMPLE-CHANNEL-UPSTREAM-HANDLER

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent event)
	throws Exception
	{
		Request request = createRequest((HttpRequest) event.getMessage(), ctx);
		Response response = createResponse(request);

		try
		{
			Action action = routeResolver.resolve(request);
			action.applyParameterHeaders(request);
			request.setResolvedRoute(action.getRoute());
			invokePreprocessors(request);
			Object result = action.invoke(request, response);
			
			if (action.shouldSerializeResponse() && hasSerializationResolver())
			{
				SerializationProcessor p = serializationResolver.resolve(request);
				response.setBody(serializeResult(result, p, request));
				response.addHeader(CONTENT_TYPE, p.getResultingContentType());
			}
			else
			{
				response.addHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");
				response.setBody(result);
			}

			invokePostprocessors(request, response);
		}
		catch (ServiceException e)
		{
			e.printStackTrace();
			response.setResponseStatus(e.getHttpStatus());
			response.setException(e);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ServiceException se = exceptionMap.getExceptionFor(e);
			
			if (se != null)
			{
				response.setResponseStatus(se.getHttpStatus());
				response.setException(se);
			}
			else
			{
				response.setResponseStatus(INTERNAL_SERVER_ERROR);
				response.setException(e);
			}
		}
		finally
		{
			if (response.hasException())
			{
				writeError(ctx, request, response);
			}
			else
			{
				// Set response and accept headers, if appropriate.
				writeResponse(ctx, request, response);
			}
		}
	}
	
	private boolean hasSerializationResolver()
	{
		return (serializationResolver != null);
	}

	/**
	 * Handles the JSONP case, wrapping the serialized JSON string in the callback function, if applicable.
	 * If there is a JSONP header value and the serialization processor returns application/json, then
	 * the serialization results are wrapped in the JSONP method call.
	 * 
	 * @param result
	 * @param procesor
	 * @param request
	 * @return
	 */
	private Object serializeResult(Object result, SerializationProcessor processor, Request request)
	{
		String serialized = processor.serialize(result);
		String callback = request.getHeader(JSONP_CALLBACK);
		
		if (callback != null 
			&& processor.getResultingContentType().toLowerCase().contains("json"))
		{
        	StringBuilder sb = new StringBuilder();
        	sb.append(callback).append("(").append(serialized).append(")");
        	return sb.toString();			
		}
		
		return serialized;
	}
	
	public void addPreprocessor(Preprocessor handler)
	{
		if (!preprocessors.contains(handler))
		{
			preprocessors.add(handler);
		}
	}

	public void addPostprocessor(Postprocessor handler)
	{
		if (!postprocessors.contains(handler))
		{
			postprocessors.add(handler);
		}
	}

	/**
     * @param request
     */
	@Override
    public void invokePreprocessors(Request request)
    {
		for (Preprocessor handler : preprocessors)
		{
			handler.process(request);
		}

		request.getBody().resetReaderIndex();
    }

	/**
     * @param request
     * @param response
     */
	@Override
    public void invokePostprocessors(Request request, Response response)
    {
		for (Postprocessor handler : postprocessors)
		{
			handler.process(request, response);
		}
    }

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent event)
	throws Exception
	{
		event.getCause().printStackTrace();
		event.getChannel().close();
	}

	/**
     * @param request
     * @return
     */
    private Request createRequest(HttpRequest request, ChannelHandlerContext context)
    {
    	return new Request(request, serializationResolver, routeResolver);
    }

	/**
     * @param request
     * @return
     */
    private Response createResponse(Request request)
    {
    	return new Response(request);
    }

    /**
     * @param message
     * @return
     */
    private void writeResponse(ChannelHandlerContext ctx, Request request, Response response)
    {
    	getResponseWriter().write(ctx, request, response);
    }

	private void writeError(ChannelHandlerContext ctx, Request request, Response response)
	{
		getErrorResponseWriter().write(ctx, request, response);
	}
}
