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
package com.strategicgains.restx.url;

import com.strategicgains.restx.service.Request;
import com.strategicgains.restx.service.Response;
import com.strategicgains.restx.service.Service;
import com.strategicgains.restx.service.exception.ServiceException;

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
	private Service service;

	// SECTION: CONSTRUCTORS

	/**
	 * @param urlPattern
	 * @param service
	 */
	public Route(UrlPattern urlPattern, Service service)
	{
		super();
		this.urlPattern = urlPattern;
		this.service = service;
	}
	
	public Route(String urlPattern, Service service)
	{
		this(new UrlPattern(urlPattern), service);
	}

	public UrlMatch match(String url)
	{
		return urlPattern.match(url);
	}

	public Object invoke(Request request, Response response)
	throws ServiceException
	{
		return service.process(request, response);
	}
}
