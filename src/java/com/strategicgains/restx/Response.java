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

package com.strategicgains.restx;

import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author toddf
 * @since Nov 20, 2009
 */
public class Response
{
	// SECTION: INSTANCE VARIABLES

	private HttpResponseStatus responseCode = OK;
	private Throwable exception = null;
	private Object body;
	private Map<String, Object> headers = new HashMap<String, Object>();

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

	public void setHeader(String name, Object value)
	{
		headers.put(name, value);
	}

	public void setResponseCode(int value)
	{
		setResponseStatus(HttpResponseStatus.valueOf(value));
	}
	
	public void setResponseStatus(HttpResponseStatus status)
	{
		this.responseCode = status;
	}
	
	public HttpResponseStatus getStatus()
	{
		return responseCode;
	}
	
	public Throwable getException()
	{
		return exception;
	}
	
	public boolean hasException()
	{
		return (getException() != null);
	}

	public void setException(Throwable t)
	{
		this.exception = t;
	}
	
	public String getResponseMessage()
	{
		Throwable cause = getException();
		Throwable current = cause;

		while (current != null)
		{
			cause = current;
			current = current.getCause();
		}

		return cause.getMessage();
	}
}
