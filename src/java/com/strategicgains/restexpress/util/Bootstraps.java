package com.strategicgains.restexpress.util;

import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * static helper class to automate getting Netty going
 * @author kevwil
 * @since October 1, 2010
 */
public final class Bootstraps
{
	// no instances, just static methods
	private Bootstraps(){}
	
	/**
	 * Build up a server with NIO channels and default cached thread pools.
	 * 
	 * @see ServerBootstrap
	 * @see NioServerSocketChannelFactory
	 * @see Executors
	 * @return An {@link ServerBootstrap} instance.
	 */
	public final static ServerBootstrap createServerNioBootstrap()
	{
		return new ServerBootstrap(
			    new NioServerSocketChannelFactory(
				    	Executors.newCachedThreadPool(),
				        Executors.newCachedThreadPool()));
	}
}
