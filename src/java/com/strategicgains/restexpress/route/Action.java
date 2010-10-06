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

import java.util.Map.Entry;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.url.UrlMatch;

/**
 * @author toddf
 * @since July 30, 2010
 */
public class Action
{
	private Route route;
	private UrlMatch match;
	
	public Action(Route route, UrlMatch match)
	{
		super();
		this.route = route;
		this.match = match;
	}

	public Route getRoute()
	{
		return route;
	}
	
	/**
	 * Returns whether the underlying Route should serialize the response.
	 * 
	 * @return
	 */
	public boolean shouldSerializeResponse()
	{
		return getRoute().shouldSerializeResponse();
	}
    
	/**
	 * Invokes the underlying Route, returning the result of the call, if any.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
    public Object invoke(Request request, Response response)
    {
//		applyParameterHeaders(request);
    	return getRoute().invoke(request, response);
    }

	/**
	 * Applies the parameters from the URL match to the headers of the request before invocation.
	 * 
     * @param parameters a Set of Entry<String, String> name/value pairs of parameters parsed from the URL.
     * @param request the Request instance in which to set parameter headers.
     */
    public void applyParameterHeaders(Request request)
    {
    	if (match == null) return;

    	for (Entry<String, String> entry : match.parameterSet())
    	{
    		request.addHeader(entry.getKey(), entry.getValue());
    	}
    }
}
