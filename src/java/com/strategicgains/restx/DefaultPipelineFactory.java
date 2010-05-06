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

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

import com.strategicgains.restx.service.Resolver;
import com.strategicgains.restx.service.Service;

/**
 * @author Todd Fredrich
 * @since Nov 13, 2009
 */
public class DefaultPipelineFactory
implements ChannelPipelineFactory
{
	// SECTION: CONSTANTS

	private static final int DEFAULT_MAX_CHUNK_SIZE = 1048576;

	
	// SECTION: INSTANCE VARIABLES

	private boolean shouldUseSsl = false;
	private boolean shouldHandleChunked = false;
	private Resolver<Service> serviceResolver;

	
	// SECTION: CONSTRUCTORS

	public DefaultPipelineFactory()
	{
		this(false, false);
	}

	public DefaultPipelineFactory(boolean useSsl, boolean useChunked)
	{
		super();
		this.shouldUseSsl = useSsl;
		this.shouldHandleChunked = useChunked;
	}

	
	// SECTION: CHANNEL PIPELINE FACTORY

	@Override
	public ChannelPipeline getPipeline() throws Exception
	{
		// Create a default pipeline implementation.
		ChannelPipeline pipeline = Channels.pipeline();

		if (shouldUseSsl)
		{
//			SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
//			engine.setUseClientMode(false);
//			pipeline.addLast("ssl", new SslHandler(engine));
		}

		pipeline.addLast("decoder", new HttpRequestDecoder());

		if(shouldHandleChunked)
		{
			pipeline.addLast("aggregator", new HttpChunkAggregator(DEFAULT_MAX_CHUNK_SIZE));
		}

		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("handler", new DefaultRequestHandler(serviceResolver));
//		pipeline.addLast("handler", new HttpRequestHandler());

		return pipeline;
	}
}
