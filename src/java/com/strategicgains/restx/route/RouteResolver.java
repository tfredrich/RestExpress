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
package com.strategicgains.restx.route;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.strategicgains.restx.Request;
import com.strategicgains.restx.exception.BadRequestException;
import com.strategicgains.restx.url.UrlMatch;
import com.strategicgains.restx.util.Resolver;

/**
 * @author toddf
 * @since May 4, 2010
 */
public class RouteResolver
implements Resolver<Action>
{
	private RouteMapping routes;
	
	public RouteResolver(RouteMapping routes)
	{
		super();
		this.routes = routes.initialize();
	}
	
	public Route getNamedRoute(String name, HttpMethod method)
	{
		return routes.getNamedRoute(name, method);
	}
	
	@Override
	public Action resolve(Request request)
	{
		for (Route route : routes.getRoutesFor(request.getRealMethod()))
		{
			UrlMatch match = route.match(request.getUrl());

			if (match != null)
			{
				return new Action(route, match);
			}
		}

		throw new BadRequestException("Unresolvable URL: " + request.getUrl());
	}
}
