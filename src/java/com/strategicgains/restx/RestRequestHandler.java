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

/**
 * @author toddf
 * @since Nov 13, 2009
 */
@ChannelPipelineCoverage("all")
public class RestRequestHandler
extends SimpleChannelUpstreamHandler
{
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
	throws Exception
	{
		// Determine which service to call via URL & parameters.
		// Throw exception if no service found/available.
		// Deserialize/marshal the request contents, if necessary.
		// Call the service, passing the marshaled object(s).
		// Serialize/Unmarshal the response, if necessary.
		// Set resonse and accept headers, if appropriate.
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
	throws Exception
	{
		e.getCause().printStackTrace();
		e.getChannel().close();
	}
}
