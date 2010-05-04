/*
 * Copyright 2009, Strategic Gains, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.strategicgains.restx.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author toddf
 * @since Nov 20, 2009
 */
public class Request
{
	// SECTION: INSTANCE VARIABLES

	private Object body;
	private Map<String, Object> headers = new HashMap<String, Object>();
	private Map<String, String> parameters = new HashMap<String, String>();

	
	// SECTION: ACCESSORS/MUTATORS

	public Object getBody()
    {
    	return body;
    }
	
	public boolean hasBody()
	{
		return (getBody() != null);
	}

	public void setBody(Object body)
    {
    	this.body = body;
    }

	public void clearHeaders()
	{
		headers.clear();
	}

	public Object getHeader(String name)
	{
		return headers.get(name);
	}

	public boolean hasHeaders()
	{
		return !headers.isEmpty();
	}

	public Map<String, Object> getHeaders()
    {
    	return Collections.unmodifiableMap(headers);
    }
	
	public Object setHeader(String name, Object value)
    {
		return headers.put(name, value);
    }
	
	public void clearParameters()
	{
		parameters.clear();
	}
	
	public String getParameter(String name)
	{
		return parameters.get(name);
	}
	
	public boolean hasParameters()
	{
		return !parameters.isEmpty();
	}
	
	public Map<String, String> getParameters()
	{
		return Collections.unmodifiableMap(parameters);
	}
	
	public String setParameter(String name, String value)
	{
		return parameters.put(name, value);
	}
	
	public String getUrl()
	{
		// TODO: return the request URL.
		return null;
	}
}
