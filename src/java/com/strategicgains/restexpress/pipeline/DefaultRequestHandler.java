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

import static com.strategicgains.restexpress.ContentType.TEXT_PLAIN;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.domain.Error;
import com.strategicgains.restexpress.exception.ExceptionMapping;
import com.strategicgains.restexpress.exception.ServiceException;
import com.strategicgains.restexpress.response.DefaultHttpResponseWriter;
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
	private List<Preprocessor> preprocessors = new ArrayList<Preprocessor>();
	private List<Postprocessor> postprocessors = new ArrayList<Postprocessor>();
	private ExceptionMapping exceptionMap = new ExceptionMapping();
	private List<MessageObserver> messageObservers = new ArrayList<MessageObserver>();


	// SECTION: CONSTRUCTORS

	public DefaultRequestHandler(RouteResolver routeResolver, Resolver<SerializationProcessor> serializationResolver)
	{
		this(routeResolver, serializationResolver, new DefaultHttpResponseWriter());
	}

	public DefaultRequestHandler(RouteResolver routeResolver, Resolver<SerializationProcessor> serializationResolver,
		HttpResponseWriter responseWriter)
	{
		super();
		this.routeResolver = routeResolver;
		this.serializationResolver = serializationResolver;
		setResponseWriter(responseWriter);
	}


	// SECTION: MUTATORS
	
	public void addMessageObserver(MessageObserver... observers)
	{
		for (MessageObserver observer : observers)
		{
			if (!messageObservers.contains(observer))
			{
				messageObservers.add(observer);
			}
		}
	}
	
	public <T extends Exception, U extends ServiceException> DefaultRequestHandler mapException(Class<T> from, Class<U> to)
	{
		exceptionMap.map(from, to);
		return this;
	}

	public HttpResponseWriter getResponseWriter()
	{
		return this.responseWriter;
	}

	public void setResponseWriter(HttpResponseWriter writer)
	{
		this.responseWriter = writer;
	}


	// SECTION: SIMPLE-CHANNEL-UPSTREAM-HANDLER

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent event)
	throws Exception
	{
		MessageContext context = createInitialContext(ctx, event);
		notifyReceived(context);
		resolveRoute(context);
		invokePreprocessors(context.getRequest());
		Object result = context.getAction().invoke(context.getRequest(), context.getResponse());
		
		if (result != null)
		{
			context.getResponse().setBody(result);
		}

		invokePostprocessors(context.getRequest(), context.getResponse());
		serializeResponse(context);
		writeResponse(ctx, context);
		notifySuccess(context);
		notifyComplete(context);
	}

	private MessageContext createInitialContext(ChannelHandlerContext ctx, MessageEvent event)
	{
		Request request = createRequest((HttpRequest) event.getMessage(), ctx);
		Response response = createResponse(request);
		MessageContext context = new MessageContext(request, response);
		context.setSerializationProcessor(serializationResolver.resolve(context.getRequest()));
		ctx.setAttachment(context);
		return context;
	}

	private void resolveRoute(MessageContext context)
    {
	    Action action = routeResolver.resolve(context.getRequest());
		context.setAction(action);
		context.setSerializationProcessor(serializationResolver.resolve(context.getRequest()));
    }


    /**
     * @param request
     * @param response
     */
    private void notifyReceived(MessageContext context)
    {
    	for (MessageObserver observer : messageObservers)
    	{
    		observer.onReceived(context.getRequest(), context.getResponse());
    	}
    }

	/**
     * @param request
     * @param response
     */
    private void notifyComplete(MessageContext context)
    {
    	for (MessageObserver observer : messageObservers)
    	{
    		observer.onComplete(context.getRequest(), context.getResponse());
    	}
    }

	// SECTION: UTILITY -- PRIVATE

	/**
     * @param exception
     * @param request
     * @param response
     */
    private void notifyException(Throwable exception, MessageContext context)
    {
    	for (MessageObserver observer : messageObservers)
    	{
    		observer.onException(exception, context.getRequest(), context.getResponse());
    	}
    }

	/**
     * @param request
     * @param response
     */
    private void notifySuccess(MessageContext context)
    {
    	for (MessageObserver observer : messageObservers)
    	{
    		observer.onSuccess(context.getRequest(), context.getResponse());
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
		MessageContext context = (MessageContext) ctx.getAttachment();
		notifyException(event.getCause(), context);
		serializeException(event.getCause(), context);
		writeResponse(ctx, context);
		notifyComplete(context);
	}

	/**
     * @param request
     * @return
     */
    private Request createRequest(HttpRequest request, ChannelHandlerContext context)
    {
    	return new Request(request, routeResolver);
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
    private void writeResponse(ChannelHandlerContext ctx, MessageContext context)
    {
    	getResponseWriter().write(ctx, context.getRequest(), context.getResponse());
    }

	private void serializeResponse(MessageContext context)
	{
		if (context.shouldSerializeResponse() && hasSerializationResolver())
		{
			SerializationProcessor sp = context.getSerializationProcessor();
			Request request = context.getRequest();
			Response response = context.getResponse();
			response.setBody(serializeResult(response.getBody(), sp, request));
		}

		String contentType = (context.getContentType() == null ? TEXT_PLAIN : context.getContentType());
		context.getResponse().addHeader(CONTENT_TYPE, contentType);
	}

	private void serializeException(Throwable throwable, MessageContext context)
	{
		ServiceException se = null;
		Error error = null;

		if (ServiceException.class.isAssignableFrom(throwable.getClass()))
		{
			se = (ServiceException) throwable;
		}
		else
		{
			se = exceptionMap.getExceptionFor(throwable);
		}

		if (se != null)
		{
			context.getResponse().setResponseStatus(se.getHttpStatus());
			error = new Error(se.getMessage());
		}
		else
		{
			context.getResponse().setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
			error = new Error(throwable.getMessage());
		}
		
		context.getResponse().setBody(error);
		serializeResponse(context);
	}
	
	

//	private void yada()
//	{
//		PipelineContext context = createRequestContext(ctx, event);
//		ctx.setAttachment(context);
//		notifyReceived(context.getRequest(), context.getResponse());
//
//		try
//		{
//			invokePreprocessors(context.getRequest());
//			Object result = context.getAction().invoke(context.getRequest(), context.getResponse());
//			invokePostprocessors(context.getRequest(), context.getResponse());
//			notifyComplete(request, response);
//		}
//		catch (ServiceException e)
//		{
//			e.printStackTrace();
//			response.setResponseStatus(e.getHttpStatus());
//			response.setException(e);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//			ServiceException se = exceptionMap.getExceptionFor(e);
//			
//			if (se != null)
//			{
//				response.setResponseStatus(se.getHttpStatus());
//				response.setException(se);
//			}
//			else
//			{
//				response.setResponseStatus(INTERNAL_SERVER_ERROR);
//				response.setException(e);
//			}
//		}
//		finally
//		{
////			Result result = Result.fromResponse(response);
//			// TODO: marshal the result into the response body.
//
//			if (response.hasException())
//			{
//				notifyException(response.getException(), request, response);
//				writeError(ctx, request, response);
//			}
//			else
//			{
//				notifySuccess(request, response);
//				// Set response and accept headers, if appropriate.
//				writeResponse(ctx, request, response);
//			}
//
//			notifyComplete(request, response);
//		}
//	}
}
