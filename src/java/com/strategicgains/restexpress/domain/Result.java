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
package com.strategicgains.restexpress.domain;

import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.exception.ServiceException;

/**
 * Generic JSEND-style wrapper for responses.
 * 
 * @author toddf
 * @since Jan 11, 2011
 */
public class Result
{
	private static final String STATUS_SUCCESS = "success";
	private static final String STATUS_ERROR = "error";
	private static final String STATUS_FAIL = "fail";

	private int code;
	private String status;
	private String message;
	private Object data;

	public Result(ServiceException exception)
	{
		this(exception.getHttpStatus().getCode(), STATUS_ERROR, exception.getMessage(), exception.getStackTrace().toString());
	}
	
	public Result(Throwable throwable)
	{
		this(HttpResponseStatus.INTERNAL_SERVER_ERROR.getCode(), STATUS_FAIL, HttpResponseStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), throwable.getStackTrace().toString());
	}

	public Result(int httpResponseCode, String status, String errorMessage, Object data)
	{
		super();
		this.code = httpResponseCode;
		this.status = status;
		this.message = errorMessage;
		this.data = data;
	}

	public int getCode()
    {
    	return code;
    }
	
	public String getMessage()
	{
		return message;
	}

	public String getStatus()
    {
    	return status;
    }

	public Object getData()
    {
    	return data;
    }
	
	
	// SECTION: FACTORY
	
	public static Result successResult(Object data)
	{
		HttpResponseStatus status = HttpResponseStatus.OK;
		return new Result(status.getCode(), STATUS_SUCCESS, null, data);
	}
	
	public static Result fromResponse(Response response)
	{
		if (response.hasException())
		{
			return new Result(response.getException());
		}
		
		HttpResponseStatus status = response.getStatus();
		return new Result(status.getCode(), STATUS_SUCCESS, null, response.getBody());
	}
}
