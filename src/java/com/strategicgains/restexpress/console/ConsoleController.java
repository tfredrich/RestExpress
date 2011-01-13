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

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;

/**
 * @author toddf
 * @since Jan 12, 2011
 */
public class ConsoleController
{
	private ServiceMetadata metadata;
	private String consoleHtml = "<html><head><title>RESTExpress Service Console</title></head><body>Not yet implemented...</body></html>";

	public ConsoleController(ServiceMetadata metadata)
	{
		super();
		this.metadata = metadata;
	}

	public ServiceMetadata getMetadata(Request request, Response response)
	{
		return metadata;
	}
	
	/**
	 * Return the HTML for the Route testing console.
	 * 
	 * @param request
	 * @param response
	 * @return HTML
	 */
	public String getConsole(Request request, Response response)
	{
		return consoleHtml;
	}
}
