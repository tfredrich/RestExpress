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
package com.strategicgains.restexpress.route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.netty.handler.codec.http.HttpMethod;

/**
 * Contains the routes for a given service implementation.  Sub-classes will implement the initialize() method which
 * calls map() to specify how URL request will be routed to the underlying controllers. 
 * 
 * @author toddf
 * @since May 21, 2010
 */
public abstract class RouteMapping
{
	// SECTION: INSTANCE VARIABLES

	private Map<HttpMethod, List<Route>> routes;
	private List<Route> deleteRoutes = new ArrayList<Route>();
	private List<Route> getRoutes = new ArrayList<Route>();
	private List<Route> postRoutes = new ArrayList<Route>();
	private List<Route> putRoutes = new ArrayList<Route>();
	private List<Route> optionRoutes = new ArrayList<Route>();
	private List<Route> headRoutes = new ArrayList<Route>();

	private Map<String, Map<HttpMethod, Route>> routesByName = new HashMap<String, Map<HttpMethod,Route>>();
	private List<RouteBuilder> routeBuilders = new ArrayList<RouteBuilder>();


	// SECTION: CONSTRUCTOR

	public RouteMapping()
	{
		super();
		routes = new HashMap<HttpMethod, List<Route>>();
		routes.put(HttpMethod.DELETE, deleteRoutes);
		routes.put(HttpMethod.GET, getRoutes);
		routes.put(HttpMethod.POST, postRoutes);
		routes.put(HttpMethod.PUT, putRoutes);
		routes.put(HttpMethod.HEAD, headRoutes);
		routes.put(HttpMethod.OPTIONS, optionRoutes);
	}
    
    /**
     * Initialize MUST be called to invoke the RouteBuilder instances before the routes will be activated.
     */
    public RouteMapping initialize()
    {
    	defineRoutes();
    	
    	for (RouteBuilder builder : routeBuilders)
    	{
    		for (Route route : builder.build())
    		{
    			addRoute(route);
    		}
    	}
    	
    	// Garbage collect the builders and blow chow if buildRoutes() gets called a second time.
    	routeBuilders.clear();
    	routeBuilders = null;
    	return this;
    }

    protected abstract void defineRoutes();


	// SECTION: URL MAPPING
	
    /**
     * Map a URL pattern to a controller.
     * 
     * @param urlPattern a string specifying a URL pattern to match.
     * @param controller a pojo which contains implementations of create(), read(), update(), delete() methods.
     */
	public RouteBuilder uri(String uri, Object controller)
	{
		RouteBuilder builder = new RouteBuilder(uri, controller);
		routeBuilders.add(builder);
		return builder;
	}
	
	
	// SECTION: UTILITY - PUBLIC

	/**
	 * Return a list of Route instances for the given HTTP method.  The returned list is immutable.
	 * 
	 * @param method the HTTP method (GET, PUT, POST, DELETE) for which to retrieve the routes.
	 */
	public List<Route> getRoutesFor(HttpMethod method)
	{
		List<Route> routesFor = routes.get(method);
		
		if (routesFor == null)
		{
			return Collections.emptyList();
		}
		
		return Collections.unmodifiableList(routesFor);
	}
	
	/**
	 * Return a Route by the name and HttpMethod provided in DSL.
	 * Returns null if no route found.
	 * 
	 * @param name
	 * @return
	 */
	public Route getNamedRoute(String name, HttpMethod method)
	{
		Map<HttpMethod, Route> routesByMethod = routesByName.get(name);
		
		if (routesByMethod == null)
		{
			return null;
		}
		
		return routesByMethod.get(method);
	}


	// SECTION: UTILITY - PRIVATE

	/**
	 * @param method
	 * @param route
	 */
	private void addRoute(Route route)
	{
		routes.get(route.getMethod()).add(route);
		
		if (route.hasName())
		{
			addNamedRoute(route);
		}

		// TODO: call log4j for added route, method
	}
	
	private void addNamedRoute(Route route)
	{
		Map<HttpMethod, Route> routesByMethod = routesByName.get(route.getName());
		
		if (routesByMethod == null)
		{
			routesByMethod = new HashMap<HttpMethod, Route>();
			routesByName.put(route.getName(), routesByMethod);
		}
		
		routesByMethod.put(route.getMethod(), route);
	}
}
