/*
 * Copyright 2010, eCollege, Inc.  All rights reserved.
 */
package com.strategicgains.restexpress.pipeline;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpContentDecompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Provides a tiny DSL to define the pipeline features.
 * 
 * @author toddf
 * @since Aug 27, 2010
 */
public class PipelineBuilder
implements ChannelPipelineFactory
{
	// SECTION: CONSTANTS

	private static final int DEFAULT_MAX_CHUNK_SIZE = 1048576;

	
	// SECTION: INSTANCE VARIABLES

	private boolean shouldUseSsl = false;
	private boolean shouldHandleChunked = false;
	private boolean shouldUseCompression = false;
	private int maxChunkSize = DEFAULT_MAX_CHUNK_SIZE;
	private ChannelHandler requestHandler;

	
	// SECTION: CONSTRUCTORS

	public PipelineBuilder()
	{
		super();
	}

	
	// SECTION: BUILDER METHODS
	
	public PipelineBuilder useSsl()
	{
		this.shouldUseSsl = true;
		return this;
	}
	
	public PipelineBuilder noSsl()
	{
		this.shouldUseSsl = false;
		return this;
	}
	
	public PipelineBuilder useCompression()
	{
		this.shouldUseCompression = true;
		return this;
	}
	
	public PipelineBuilder noCompression()
	{
		this.shouldUseCompression = false;
		return this;
	}
	
	public PipelineBuilder handleChunked()
	{
		this.shouldHandleChunked = true;
		return this;
	}
	
	public PipelineBuilder noChuncked()
	{
		this.shouldHandleChunked = false;
		return this;
	}
	
	public PipelineBuilder maxChunkSize(int size)
	{
		this.maxChunkSize = size;
		return this;
	}
	
	public PipelineBuilder setRequestHandler(ChannelHandler handler)
	{
		this.requestHandler = handler;
		return this;
	}


	// SECTION: CHANNEL PIPELINE FACTORY

	@Override
	public ChannelPipeline getPipeline()
	throws Exception
	{
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
			pipeline.addLast("aggregator", new HttpChunkAggregator(maxChunkSize));
		}

		pipeline.addLast("encoder", new HttpResponseEncoder());
		
		if (shouldUseCompression)
		{
			pipeline.addLast("deflater", new HttpContentCompressor());
			pipeline.addLast("inflater", new HttpContentDecompressor());
		}

		pipeline.addLast("router", requestHandler);

		return pipeline;
	}
}
