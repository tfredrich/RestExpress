/*
 * Copyright 2010, eCollege, Inc.  All rights reserved.
 */
package com.strategicgains.restx.response;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponse;

import com.strategicgains.restx.Request;
import com.strategicgains.restx.Response;

/**
 * @author toddf
 * @since Aug 26, 2010
 */
public class ErrorHttpResponseWriter
implements HttpResponseWriter
{
	@Override
	public void write(ChannelHandlerContext ctx, Request request, Response response)
	{
		HttpResponse httpResponse = new DefaultHttpResponse(HTTP_1_1, response.getStatus());
		httpResponse.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");
		StringBuilder builder = new StringBuilder("Failure: ");
		builder.append(response.getResponseMessage());
		builder.append("\r\n");
		httpResponse.setContent(ChannelBuffers.copiedBuffer(builder.toString(), Charset.forName("UTF-8")));

		if (request.isKeepAlive())
        {
        	// Add 'Content-Length' header only for a keep-alive connection.
        	httpResponse.setHeader(CONTENT_LENGTH, String.valueOf(httpResponse.getContent().readableBytes()));
        }
		else
		{
			httpResponse.setHeader(CONNECTION, "close");
		}

		// Close the connection as soon as the error message is sent.
		ctx.getChannel().write(httpResponse).addListener(ChannelFutureListener.CLOSE);
	}
}
