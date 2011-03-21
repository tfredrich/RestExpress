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
package com.strategicgains.restexpress.controller;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.domain.console.ServerMetadata;

/**
 * @author toddf
 * @since Jan 31, 2011
 */
public class ConsoleController
{
	private ServerMetadata metadata;

	public ConsoleController(ServerMetadata data)
	{
		super();
		this.metadata = data;
	}

	public ServerMetadata getRoutes(Request request, Response response)
	{
		return metadata;
	}

	public Object getConsole(Request request, Response response)
	{
		return null;
	}
}
