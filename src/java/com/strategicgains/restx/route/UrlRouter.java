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

import java.util.Map.Entry;
import java.util.Set;

import com.strategicgains.restx.Request;
import com.strategicgains.restx.Response;
import com.strategicgains.restx.exception.BadRequestException;
import com.strategicgains.restx.url.UrlMatch;

/**
 * @author toddf
 * @since May 4, 2010
 */
public class UrlRouter
{
	private RouteMapping routes;
	
	public UrlRouter(RouteMapping routes)
	{
		super();
		this.routes = routes;
	}
	
	public Object handleUrl(Request request, Response response)
	{
		for (Route route : routes.getRoutesFor(request.getMethod()))
		{
			UrlMatch match = route.match(request.getUrl());

			if (match != null)
			{
				addParameterHeaders(match.parameterSet(), request);
				return route.invoke(request, response);
			}
		}

		throw new BadRequestException("Unserviceable URL: " + request.getUrl());
	}

	/**
     * @param parameters a Set of Entry<String, String> name/value pairs of parameters parsed from the URL.
     * @param request the Request instance in which to set parameter headers.
     */
    private void addParameterHeaders(Set<Entry<String, String>> parameters, Request request)
    {
    	for (Entry<String, String> entry : parameters)
    	{
    		request.addHeader(entry.getKey(), entry.getValue());
    	}
    }
}
