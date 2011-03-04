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
package com.strategicgains.restexpress.pipeline;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.exception.NotFoundException;

/**
 * @author toddf
 * @since Feb 10, 2011
 */
public class WrappedResponseController
{
	public Object normalGetAction(Request request, Response response)
	{
		return "Normal GET action";
	}

	public Object normalPutAction(Request request, Response response)
	{
		return "Normal PUT action";
	}

	public Object normalPostAction(Request request, Response response)
	{
		return "Normal POST action";
	}

	public Object normalDeleteAction(Request request, Response response)
	{
		return "Normal DELETE action";
	}

	public void noContentDeleteAction(Request request, Response response)
	{
		response.setResponseNoContent();
	}

	public void noContentWithBodyDeleteAction(Request request, Response response)
	{
		response.setBody("Body with no content response");
		response.setResponseNoContent();
	}

	public void notFoundAction(Request request, Response response)
	{
		throw new NotFoundException("Item not found");
	}
	
	public void nullPointerAction(Request request, Response response)
	{
		throw new NullPointerException("Null and void");
	}
}
