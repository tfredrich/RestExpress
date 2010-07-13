/*
    Copyright 2010, Strategic Gains, Inc.

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
package com.strategicgains.restx.exception;

import java.util.List;

/**
 * @author toddf
 * @since Jul 12, 2010
 */
public class ValidationException
extends BadRequestException
{
	private static final long serialVersionUID = -8379102665666124764L;

	public ValidationException()
	{
		super();
	}

	/**
	 * @param message
	 */
	public ValidationException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public ValidationException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ValidationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ValidationException(List<String> errors)
	{
		this(joinErrors(errors));
	}

	public ValidationException(List<String> errors, Throwable cause)
	{
		this(joinErrors(errors), cause);
	}
	
	protected static String joinErrors(List<String> errors)
	{
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		
		for (String error : errors)
		{
			if (! isFirst)
			{
				sb.append(", ");
			}
			
			sb.append(error);
			isFirst = false;
		}

		return sb.toString();
	}
}
