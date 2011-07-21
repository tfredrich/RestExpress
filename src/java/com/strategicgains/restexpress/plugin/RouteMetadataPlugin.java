/*
    Copyright 2011, Strategic Gains, Inc.

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
package com.strategicgains.restexpress.plugin;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.controller.ConsoleController;
import com.strategicgains.restexpress.domain.console.RouteMetadata;
import com.strategicgains.restexpress.domain.console.ServerMetadata;
import com.strategicgains.restexpress.route.RouteBuilder;

/**
 * @author toddf
 * @since Jul 20, 2011
 */
public class RouteMetadataPlugin
extends AbstractPlugin
{
	private static final String DEFAULT_CONSOLE_PREFIX = "/console";

	private String prefix;
	private ConsoleController controller;

	public RouteMetadataPlugin()
	{
		this(DEFAULT_CONSOLE_PREFIX);
	}

	public RouteMetadataPlugin(String routePrefix)
	{
		super();
		this.prefix = routePrefix;
		this.controller = new ConsoleController();
	}

    @Override
    public RouteBuilder register(RestExpress server)
    {
    	super.register(server);
		RouteBuilder builder = server.getRouteDeclarations()
			.uri(prefix + "/routes.{format}", controller)
			.action("getRoutes", HttpMethod.GET);
		server.alias("service", ServerMetadata.class);
		server.alias("route", RouteMetadata.class);
    	return builder;
    }

    @Override
    public void bind(RestExpress server)
    {
    	controller.setMetadata(server.getRouteMetadata());
    }
}
