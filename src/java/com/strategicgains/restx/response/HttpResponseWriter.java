/*
 * Copyright 2010, eCollege, Inc.  All rights reserved.
 */
package com.strategicgains.restx.response;

import org.jboss.netty.channel.ChannelHandlerContext;

import com.strategicgains.restx.Request;
import com.strategicgains.restx.Response;

/**
 * @author toddf
 * @since Aug 26, 2010
 */
public interface HttpResponseWriter
{
	public void write(ChannelHandlerContext ctx, Request request, Response response);
}
