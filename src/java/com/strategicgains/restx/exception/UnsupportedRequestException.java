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

package com.strategicgains.restx.exception;

import org.jboss.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author toddf
 * @since Nov 20, 2009
 */
public class UnsupportedRequestException
extends ServiceException
{
    private static final long serialVersionUID = 1322585725650252682L;

	public UnsupportedRequestException()
	{
		super(HttpResponseStatus.BAD_REQUEST);
	}

	/**
	 * @param message
	 */
	public UnsupportedRequestException(String message)
	{
		super(HttpResponseStatus.BAD_REQUEST, message);
	}

	/**
	 * @param cause
	 */
	public UnsupportedRequestException(Throwable cause)
	{
		super(HttpResponseStatus.BAD_REQUEST, cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnsupportedRequestException(String message, Throwable cause)
	{
		super(HttpResponseStatus.BAD_REQUEST, message, cause);
	}
}
