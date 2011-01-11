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

import com.strategicgains.restexpress.exception.ServiceException;

/**
 * @author toddf
 * @since Jan 11, 2011
 */
public class Result
{
	private int responseCode;
	private String message;
	private Object data;

	public Result(ServiceException exception)
	{
		this(exception.getHttpStatus().getCode(), exception.getHttpStatus().getReasonPhrase(), null);
	}

	public Result(int responseCode, String message, Object data)
	{
		super();
		this.responseCode = responseCode;
		this.message = message;
		this.data = data;
	}

	public int getResponseCode()
    {
    	return responseCode;
    }

	public String getMessage()
    {
    	return message;
    }

	public Object getData()
    {
    	return data;
    }
}
