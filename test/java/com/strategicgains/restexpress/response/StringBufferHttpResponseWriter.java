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
package com.strategicgains.restexpress.response;

import java.util.Map;

import org.jboss.netty.channel.ChannelHandlerContext;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;

/**
 * @author toddf
 * @since Feb 10, 2011
 */
public class StringBufferHttpResponseWriter
implements HttpResponseWriter
{
	private Map<String, String> headers;
	private StringBuffer body;

	public StringBufferHttpResponseWriter(StringBuffer buffer)
	{
		this(null, buffer);
	}

	public StringBufferHttpResponseWriter(Map<String, String> headers, StringBuffer buffer)
	{
		super();
		this.body = buffer;
		this.headers = headers;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Request request, Response response)
	{
		if (headers != null)
		{
			for (String headerName : response.getHeaderNames())
			{
				headers.put(headerName, response.getHeader(headerName));
			}
		}

		body.append(response.getBody());
	}
}
