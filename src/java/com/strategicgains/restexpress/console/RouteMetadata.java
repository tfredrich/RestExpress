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
package com.strategicgains.restexpress.console;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.handler.codec.http.HttpMethod;

/**
 * @author toddf
 * @since Jan 12, 2011
 */
public class RouteMetadata
{
	private String name;
	private boolean isSerialized;
	private String url;
	private List<HttpMethod> methods = new ArrayList<HttpMethod>();
	private List<String> parameters = new ArrayList<String>();

	public RouteMetadata()
	{
		super();
	}
	
	public RouteMetadata(String url, String name, boolean isSerialized)
	{
		this();
		setUrl(url);
		setName(name);
		setSerialized(isSerialized);
	}
	
	public RouteMetadata(String url, String name, boolean isSerialized, HttpMethod... methods)
	{
		this(url, name, isSerialized);
		
		for(HttpMethod method : methods)
		{
			addMethod(method);
		}
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
	
	public void addMethod(HttpMethod method)
	{
		if (!methods.contains(method))
		{
			methods.add(method);
		}
	}

	public List<HttpMethod> getMethods()
	{
		return methods;
	}

	public void setMethods(List<HttpMethod> methods)
	{
		this.methods = methods;
	}

	public boolean isSerialized()
	{
		return isSerialized;
	}

	public void setSerialized(boolean isSerialized)
	{
		this.isSerialized = isSerialized;
	}
	
	public void addParameter(String parameter)
	{
		if (!parameters.contains(parameter))
		{
			parameters.add(parameter);
		}
	}

	public List<String> getParameters()
	{
		return parameters;
	}

	public void setParameters(List<String> parameters)
	{
		this.parameters = parameters;
	}
}
