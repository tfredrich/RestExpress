/*
    Copyright 2010, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
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
