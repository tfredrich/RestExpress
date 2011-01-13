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
package com.strategicgains.restexpress.console;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.strategicgains.restexpress.route.RouteBuilder;
import com.strategicgains.restexpress.route.RouteMapping;
import com.strategicgains.restexpress.route.RoutesDeclaration;
import com.strategicgains.restexpress.route.parameterized.ParameterizedRouteBuilder;

/**
 * @author toddf
 * @since Jan 12, 2011
 */
public class Console
{
	public static void initialize(RoutesDeclaration declarations, RouteMapping mapping)
	{
		ServiceMetadata metadata = new ServiceMetadata();
		metadata.setRoutes(declarations.getRouteMetadata());
		ConsoleController consoleController = new ConsoleController(metadata);
		defineRoutes(mapping, consoleController);
	}

	private static void defineRoutes(RouteMapping routes, ConsoleController controller)
	{
		RouteBuilder metaBuilder = new ParameterizedRouteBuilder("/_routes/_meta.{format}", controller)
			.action("getMetadata", HttpMethod.GET);
		routes.addRoute(metaBuilder.build().get(0));

		RouteBuilder consoleBuilder = new ParameterizedRouteBuilder("/_routes/_console.{format}", controller)
			.action("getConsole", HttpMethod.GET);
		routes.addRoute(consoleBuilder.build().get(0));
	}
}
