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

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.strategicgains.restx.route.Resolver;
import com.strategicgains.restx.route.Service;

/**
 * @author toddf
 * @since Nov 13, 2009
 */
@ChannelPipelineCoverage("all")
public class DefaultRequestHandler
extends SimpleChannelUpstreamHandler
{
	// SECTION: INSTANCE VARIABLES

	private Resolver resolver;


	// SECTION: CONSTRUCTORS

	public DefaultRequestHandler(Resolver resolver)
	{
		super();
		setResolver(resolver);
	}

	
	// SECTION: ACCESSORS/MUTATORS

	/**
	 * @return the resolver
	 */
	public Resolver getResolver()
	{
		return resolver;
	}

	/**
	 * @param resolver
	 *            the resolver to set
	 */
	public void setResolver(Resolver resolver)
	{
		this.resolver = resolver;
	}


	// SECTION: SIMPLE-CHANNEL-UPSTREAM-HANDLER

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent event)
	throws Exception
	{
		// Determine which service to call via URL & parameters.
		// Throw exception if no service found/available.
		HttpRequest request = (HttpRequest) event.getMessage();
		Service service = resolver.resolve(request);

		// Deserialize/marshal the request contents, if necessary.
		// Call the service, passing the marshaled object(s).
		service.process(request, service.deserialize(request));

		// Serialize/Unmarshal the response, if necessary.
		// Set resonse and accept headers, if appropriate.
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent event)
	throws Exception
	{
		event.getCause().printStackTrace();
		event.getChannel().close();
	}
}
