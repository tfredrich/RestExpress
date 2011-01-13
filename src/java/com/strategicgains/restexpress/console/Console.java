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

import com.strategicgains.restexpress.route.RouteMapping;

/**
 * @author toddf
 * @since Jan 12, 2011
 */
public class Console
{
	public static void initialize(RouteMapping routes)
	{
		
		ConsoleController consoleController = new ConsoleController(routes.asServiceMetadata());
		defineRoutes(routes, consoleController);
	}

	private static void defineRoutes(RouteMapping routes, ConsoleController controller)
	{
		routes.uri("/_routes/_meta.{format}", controller)
			.action("getMetadata", HttpMethod.GET);

		routes.uri("/_routes/_console.{format}", controller)
			.action("getConsole", HttpMethod.GET);
	}
}
