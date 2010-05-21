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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.strategicgains.restx.Request;
import com.strategicgains.restx.Response;
import com.strategicgains.restx.exception.ConfigurationException;

/**
 * @author toddf
 * @since May 21, 2010
 */
public abstract class RouteMapping
{
	// SECTION: CONSTANTS

	static final String DELETE_ACTION_NAME = "delete";
	static final String GET_ACTION_NAME = "read";
	static final String POST_ACTION_NAME = "create";
	static final String PUT_ACTION_NAME = "update";
	static final Map<HttpMethod, String> ACTION_MAPPING = new HashMap<HttpMethod, String>();

	static
	{
		ACTION_MAPPING.put(HttpMethod.DELETE, DELETE_ACTION_NAME);
		ACTION_MAPPING.put(HttpMethod.GET, GET_ACTION_NAME);
		ACTION_MAPPING.put(HttpMethod.POST, POST_ACTION_NAME);
		ACTION_MAPPING.put(HttpMethod.PUT, PUT_ACTION_NAME);
	}


	// SECTION: INSTANCE VARIABLES

	Map<HttpMethod, List<Route>> routes;
	private List<Route> deleteRoutes = new ArrayList<Route>();
	private List<Route> getRoutes = new ArrayList<Route>();
	private List<Route> postRoutes = new ArrayList<Route>();
	private List<Route> putRoutes = new ArrayList<Route>();


	// SECTION: CONSTRUCTOR

	public RouteMapping()
	{
		super();
		routes = new HashMap<HttpMethod, List<Route>>();
		routes.put(HttpMethod.DELETE, deleteRoutes);
		routes.put(HttpMethod.GET, getRoutes);
		routes.put(HttpMethod.POST, postRoutes);
		routes.put(HttpMethod.PUT, putRoutes);
	}


	// SECTION: URL MAPPING

	public void map(String urlPattern, Object controller)
	{
		map(urlPattern, controller, GET_ACTION_NAME, HttpMethod.GET);
		map(urlPattern, controller, PUT_ACTION_NAME, HttpMethod.PUT);
		map(urlPattern, controller, POST_ACTION_NAME, HttpMethod.POST);
		map(urlPattern, controller, DELETE_ACTION_NAME, HttpMethod.DELETE);
	}

	public void map(String urlPattern, Object controller, HttpMethod method)
	{
		map(urlPattern, controller, ACTION_MAPPING.get(method), method);
	}

	public void map(String urlPattern, Object controller, String actionName, HttpMethod method)
	{
		Method action = determineAction(controller, actionName);
		addRoute(method, new Route(urlPattern, controller, action));
	}
	
	
	// SECTION: UTILITY - PUBLIC
	
	public List<Route> getRoutesFor(HttpMethod method)
	{
		return Collections.unmodifiableList(routes.get(method));
	}


	// SECTION: UTILITY - PRIVATE

	/**
	 * @param controller
	 * @param actionName
	 * @return
	 */
	Method determineAction(Object controller, String actionName)
	{
		try
		{
			return controller.getClass().getMethod(actionName, Request.class, Response.class);
		}
		catch (Exception e)
		{
			throw new ConfigurationException(e);
		}
	}

	/**
	 * @param method
	 * @param route
	 */
	void addRoute(HttpMethod method, Route route)
	{
		routes.get(method).add(route);
	}
}
