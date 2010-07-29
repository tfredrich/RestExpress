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

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * The main entry-point into RestX.
 * 
 * @author toddf
 * @since Nov 13, 2009
 */
public class RestServer
{
	private static final int DEFAULT_PORT = 8080;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		int port = DEFAULT_PORT;

		if (args.length > 0)
		{
			port = Integer.parseInt(args[0]);
		}

		// Configure the server.
		ServerBootstrap bootstrap = new ServerBootstrap(
		    new NioServerSocketChannelFactory(
		    	Executors.newCachedThreadPool(),
		        Executors.newCachedThreadPool()));

		// Set up the event pipeline factory.
		bootstrap.setPipelineFactory(new DefaultPipelineFactory(null /* RouteMapping instance here */));

		// Bind and start to accept incoming connections.
		System.out.println("Starting RestX Server on port " + port);
		bootstrap.bind(new InetSocketAddress(port));
	}
}
