/*
 * Copyright 2010, eCollege, Inc.  All rights reserved.
 */
package com.strategicgains.restexpress.response;

import org.jboss.netty.channel.ChannelHandlerContext;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;

/**
 * @author toddf
 * @since Aug 26, 2010
 */
public interface HttpResponseWriter
{
	public void write(ChannelHandlerContext ctx, Request request, Response response);
}
