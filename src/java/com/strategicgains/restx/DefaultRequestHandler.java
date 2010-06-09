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
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

import com.strategicgains.restx.exception.ServiceException;
import com.strategicgains.restx.route.UrlRouter;

/**
 * @author toddf
 * @since Nov 13, 2009
 */
@ChannelPipelineCoverage("all")
public class DefaultRequestHandler
extends SimpleChannelUpstreamHandler
{
	// SECTION: INSTANCE VARIABLES

	private UrlRouter urlRouter;


	// SECTION: CONSTRUCTORS

	public DefaultRequestHandler(UrlRouter urlRouter)
	{
		super();
		this.urlRouter = urlRouter;
	}


	// SECTION: SIMPLE-CHANNEL-UPSTREAM-HANDLER

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent event)
	throws Exception
	{
		// Determine which service to call via URL & parameters.
		// Throw exception if no service found/available.
		Request request = createRequest((HttpRequest) event.getMessage(), ctx);
		Response response = createResponse(request);

		try
		{
			// Deserialize/marshal the request contents, if necessary.
			// Call the service, passing the marshaled object(s).
			Object result = urlRouter.handleUrl(request, response);
			response.setBody(result);
		}
		catch (ServiceException e)
		{
			response.setResponseStatus(e.getHttpStatus());
			response.setException(e);
		}
		catch (Throwable t)
		{
			response.setResponseStatus(INTERNAL_SERVER_ERROR);
			response.setException(t);
		}
		finally
		{
			if (response.hasException())
			{
				writeError(ctx, response);
			}
			else
			{
				// Set response and accept headers, if appropriate.
				writeResponse(ctx, response);
			}
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
    	return new Request(request);
    }

	/**
     * @param request
     * @return
     */
    private Response createResponse(Request request)
    {
    	return new Response();
    }

    /**
     * @param message
     * @return
     */
    private void writeResponse(ChannelHandlerContext ctx, Response response)
    {
		HttpResponse httpResponse = new DefaultHttpResponse(HTTP_1_1, response.getStatus());
		
		if (response.hasBody())
		{
			httpResponse.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");
			StringBuilder builder = new StringBuilder(response.getBody().toString());
			builder.append("\r\n");

			httpResponse.setContent(ChannelBuffers.copiedBuffer(builder.toString(), "UTF-8"));
		}

//		if (keepAlive)
//  	{
//  		// Add 'Content-Length' header only for a keep-alive connection.
//  		httpResponse.setHeader(CONTENT_LENGTH, response.getContent().readableBytes());
//  	}
//		else
//		{
			httpResponse.setHeader(CONNECTION, "close");
//		}

		// Close the connection as soon as the error message is sent.
		ctx.getChannel().write(httpResponse).addListener(
		    ChannelFutureListener.CLOSE);
    }

	private void writeError(ChannelHandlerContext ctx, Response response) //ChannelHandlerContext ctx, HttpResponseStatus status)
	{
		HttpResponse httpResponse = new DefaultHttpResponse(HTTP_1_1, response.getStatus());
		httpResponse.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");
		StringBuilder builder = new StringBuilder("Failure: ");
		builder.append(response.getResponseMessage());
		builder.append("\r\n");

//		if (keepAlive)
//        {
//        	// Add 'Content-Length' header only for a keep-alive connection.
//        	httpResponse.setHeader(CONTENT_LENGTH, response.getContent().readableBytes());
//        }
//		else
//		{
			httpResponse.setHeader(CONNECTION, "close");
//		}

		httpResponse.setContent(ChannelBuffers.copiedBuffer(builder.toString(), "UTF-8"));

		// Close the connection as soon as the error message is sent.
		ctx.getChannel().write(httpResponse).addListener(ChannelFutureListener.CLOSE);
	}
}
