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
package com.strategicgains.restexpress.route;

import java.util.ArrayList;
import java.util.List;

import com.strategicgains.restexpress.domain.metadata.RouteMetadata;
import com.strategicgains.restexpress.route.parameterized.ParameterizedRouteBuilder;
import com.strategicgains.restexpress.route.regex.RegexRouteBuilder;

/**
 * @author toddf
 * @since Jan 13, 2011
 */
public abstract class RouteDeclaration
{
	// SECTION: INSTANCE VARIABLES

	private List<RouteBuilder> routeBuilders;
	List<RouteMetadata> routeMetadata = new ArrayList<RouteMetadata>();
	
	
	public RouteDeclaration()
	{
		super();
		this.routeBuilders = new ArrayList<RouteBuilder>();
	}


	// SECTION: URL MAPPING

	/**
	 * Map a parameterized URL pattern to a controller.
	 * 
	 * @param urlPattern a string specifying a URL pattern to match.
	 * @param controller a pojo which contains implementations of the service methods (e.g. create(), read(), update(), delete()).
	 */
	public RouteBuilder uri(String uri, Object controller)
	{
		RouteBuilder builder = new ParameterizedRouteBuilder(uri, controller);
		routeBuilders.add(builder);
		return builder;
	}

	/**
	 * Map a Regex pattern to a controller.
	 * 
	 * @param regex a string specifying a regex pattern to match.
	 * @param controller a pojo which contains implementations of service methods (e.g. create(), read(), update(), delete()).
	 */
	public RouteBuilder regex(String regex, Object controller)
	{
		RouteBuilder builder = new RegexRouteBuilder(regex, controller);
		routeBuilders.add(builder);
		return builder;
	}
	
	
	// SECTION: UTILITY - FACTORY

	/**
	 * Generate a RouteMapping (utilized by RouteResolver) from the declared routes.
	 */
	public RouteMapping createRouteMapping()
	{
		defineRoutes();
		RouteMapping results = new RouteMapping();

		for (RouteBuilder builder : routeBuilders)
		{
    		routeMetadata.add(builder.asMetadata());

    		for (Route route : builder.build())
			{
				results.addRoute(route);
			}
		}

		unDefineRoutes();
		return results;
	}

	
	// SECTION: ROUTES -SUBCLASSES

	protected abstract void defineRoutes();

	
	// SECTION: CONSOLE

	/**
     * @return
     */
    public List<RouteMetadata> getMetadata()
    {
    	return routeMetadata;
    }

	private void unDefineRoutes()
    {
	    routeBuilders.clear();
    }
}
