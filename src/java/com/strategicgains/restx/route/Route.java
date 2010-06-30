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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.strategicgains.restx.Request;
import com.strategicgains.restx.Response;
import com.strategicgains.restx.exception.ServiceException;
import com.strategicgains.restx.url.UrlMatch;
import com.strategicgains.restx.url.UrlPattern;

/**
 * A Route is an immutable relationship between a URL pattern and a REST
 * service.
 * 
 * @author toddf
 * @since May 4, 2010
 */
public class Route
{
	// SECTION: INSTANCE VARIABLES

	private UrlPattern urlPattern;
	private Object controller;
	private Method action;

	// SECTION: CONSTRUCTORS

	/**
	 * @param urlPattern
	 * @param controller
	 */
	public Route(UrlPattern urlPattern, Object controller, Method action)
	{
		super();
		this.urlPattern = urlPattern;
		this.controller = controller;
		this.action = action;
	}
	
	public Route(String urlPattern, Object controller, Method action)
	{
		this(new UrlPattern(urlPattern), controller, action);
	}

	public UrlMatch match(String url)
	{
		return urlPattern.match(url);
	}

	public Object invoke(Request request, Response response)
	{
		try
        {
	        return action.invoke(controller, request, response);
        }
		catch (InvocationTargetException e)
		{
			Throwable t = e.getCause();

			if (t instanceof ServiceException)
			{
				throw (ServiceException) t;
			}
			
			throw new ServiceException(e);
		}
        catch (Exception e)
        {
        	throw new ServiceException(e);
        }
	}
}
