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

import java.util.List;

import com.strategicgains.restexpress.route.RouteBuilder;

/**
 * @author toddf
 * @since Jan 12, 2011
 */
public class MetadataFactory
{
	private ServiceMetadata serviceMetadata = new ServiceMetadata();

	public static ServiceMetadata generate(List<RouteBuilder> builders)
	{
		MetadataFactory factory = new MetadataFactory();
		factory.process(builders);
		return factory.getServiceMetadata();
	}

	/**
	 * @param builders
	 *            a List of RouteBuilder instances.
	 */
	private void process(List<RouteBuilder> builders)
	{
		for (RouteBuilder builder : builders)
		{
			process(builder);
		}
	}

	/**
	 * @param builder
	 */
	private void process(RouteBuilder builder)
	{
		serviceMetadata.addRoute(builder.asRouteMetadata());
	}

	/**
	 * @return
	 */
	private ServiceMetadata getServiceMetadata()
	{
		return serviceMetadata;
	}
}
