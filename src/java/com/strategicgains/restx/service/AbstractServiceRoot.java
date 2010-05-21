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
package com.strategicgains.restx.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.strategicgains.restx.annotation.Endpoint;
import com.strategicgains.restx.annotation.Service;
import com.strategicgains.restx.exception.ConfigurationException;
import com.strategicgains.restx.url.Route;


/**
 * @author toddf
 * @since May 6, 2010
 */
public abstract class AbstractServiceRoot
implements ServiceRoot
{
	private Map<HttpMethod, List<Route>> routes;
	private List<Route> deleteRoutes = new ArrayList<Route>();
	private List<Route> getRoutes = new ArrayList<Route>();
	private List<Route> postRoutes = new ArrayList<Route>();
	private List<Route> putRoutes = new ArrayList<Route>();

	public AbstractServiceRoot()
	{
		super();
		routes = new HashMap<HttpMethod, List<Route>>();
		routes.put(HttpMethod.DELETE, deleteRoutes);
		routes.put(HttpMethod.GET, getRoutes);
		routes.put(HttpMethod.POST, postRoutes);
		routes.put(HttpMethod.PUT, deleteRoutes);
	}
	
	public Map<HttpMethod, List<Route>> getRoutes()
	{
		return Collections.unmodifiableMap(routes);
	}

	protected void addService(ServiceController service)
	throws ConfigurationException
	{
		Service serviceAnnotation = service.getClass().getAnnotation(Service.class);

		if (serviceAnnotation == null)
		{
			throw new ConfigurationException("ServiceController is not annotated with @Service: " + service.getClass().getCanonicalName());
		}

		String uriBase = serviceAnnotation.uriBase();
		processServiceEndpoints(service, uriBase);
	}

	private void processServiceEndpoints(ServiceController service, String uriBase)
	throws ConfigurationException
    {
	    boolean hasEndpoint = false;

		for (Method method : service.getClass().getMethods())
		{
			Endpoint endpointAnnotation = method.getAnnotation(Endpoint.class);
			
			if (endpointAnnotation != null)
			{
				hasEndpoint = true;
				addEndpointRoute(uriBase + endpointAnnotation.uriPattern(), service, method.getName());
			}
		}

		if (!hasEndpoint)
		{
			throw new ConfigurationException("No enpoints (e.g. @Endpoint annotations) found in service controller: " + service.getClass().getCanonicalName());
		}
    }

	private void addEndpointRoute(String urlPattern, ServiceController service, String methodName)
    {
	    Route route = new Route(urlPattern, service);

	    if ("read".equals(methodName))
	    	getRoutes.add(route);
	    else if ("create".equals(methodName))
	    	postRoutes.add(route);
	    else if ("update".equals(methodName))
	    	putRoutes.add(route);
	    else if ("delete".equals(methodName))
	    	deleteRoutes.add(route);
    }
}
