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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.strategicgains.restx.annotation.Get;
import com.strategicgains.restx.annotation.Service;
import com.strategicgains.restx.service.exception.ConfigurationException;
import com.strategicgains.restx.url.Route;


/**
 * @author toddf
 * @since May 6, 2010
 */
public abstract class AbstractServiceRoot
implements ServiceRoot
{
	private Map<HttpMethod, List<Route>> routes = new HashMap<HttpMethod, List<Route>>();
	private List<Route> deleteRoutes = new ArrayList<Route>();
	private List<Route> getRoutes = new ArrayList<Route>();
	private List<Route> postRoutes = new ArrayList<Route>();
	private List<Route> putRoutes = new ArrayList<Route>();

	protected void addService(Service service)
	throws ConfigurationException
	{
		// TODO: process the given Service and create a Map of UrlPattern and/or Route instances.
		// this would process the annotations on the given class instance.
		Service serviceAnnotation = service.getClass().getAnnotation(Service.class);

		if (serviceAnnotation == null)
		{
			throw new ConfigurationException("Object is not annotated with @Service: " + service.getClass().getCanonicalName());
		}
		
		String uriBase = serviceAnnotation.uriBase();
		
		for (Method method : service.getClass().getMethods())
		{
			Get get = method.getAnnotation(Get.class);
			
			if (get != null)
			{
				getRoutes.add(new Route(uriBase + get.uriPattern(), service));
			}
		}
	}
}
