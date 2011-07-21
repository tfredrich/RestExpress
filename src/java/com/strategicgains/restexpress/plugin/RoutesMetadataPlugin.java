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

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.domain.metadata.RouteMetadata;
import com.strategicgains.restexpress.domain.metadata.ServerMetadata;
import com.strategicgains.restexpress.route.RouteBuilder;

/**
 * @author toddf
 * @since Jul 20, 2011
 */
public class RoutesMetadataPlugin
extends AbstractPlugin
{
	private RouteMetadataController controller = new RouteMetadataController();
	private List<RouteBuilder> routeBuilders = new ArrayList<RouteBuilder>();

	public RoutesMetadataPlugin()
	{
		super();
	}

    @Override
    public RoutesMetadataPlugin register(RestExpress server)
    {
    	super.register(server);
    	RouteBuilder builder;
    	
		builder = server.getRouteDeclarations()
			.uri("/routes/metadata.{format}", controller)
			.action("getAllRoutes", HttpMethod.GET)
			.name("allRoutesMetadata");
		routeBuilders.add(builder);

		builder = server.getRouteDeclarations()
			.uri("/route/{routeName}/metadata.{format}", controller)
			.action("getSingleRoute", HttpMethod.GET)
			.name("singleRouteMetadata");
		routeBuilders.add(builder);

		server.getRouteDeclarations()
			.uri("/routes/console.html", controller)
			.action("getConsole", HttpMethod.GET)
			.useRawResponse()
			.noSerialization()
			.name("routesConsole");
		routeBuilders.add(builder);

		server.alias("service", ServerMetadata.class);
		server.alias("route", RouteMetadata.class);
    	return this;
    }

    @Override
    public void bind(RestExpress server)
    {
    	controller.setMetadata(server.getRouteMetadata());
    }

    // RouteBuilder route augmentation delegates.

	public RoutesMetadataPlugin flag(String flagValue)
    {
		for (RouteBuilder routeBuilder : routeBuilders)
		{
		    routeBuilder.flag(flagValue);
		}

	    return this;
    }

	public RoutesMetadataPlugin parameter(String name, Object value)
    {
		for (RouteBuilder routeBuilder : routeBuilders)
		{
		    routeBuilder.parameter(name, value);
		}

	    return this;
    }
}
