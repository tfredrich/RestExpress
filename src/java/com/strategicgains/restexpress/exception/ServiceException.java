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

package com.strategicgains.restexpress.exception;

import org.jboss.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author toddf
 * @since Nov 20, 2009
 */
public class ServiceException
extends RuntimeException
{
    private static final long serialVersionUID = 1810995969641082808L;
    private static final HttpResponseStatus STATUS = HttpResponseStatus.INTERNAL_SERVER_ERROR;
    
    private HttpResponseStatus httpStatus;

	public ServiceException()
	{
		this(STATUS);
	}
	
	protected ServiceException(HttpResponseStatus status)
	{
		setHttpStatus(status);
	}

	/**
	 * @param message
	 */
	public ServiceException(String message)
	{
		this(STATUS, message);
	}
	
	protected ServiceException(HttpResponseStatus status, String message)
	{
		super(message);
		setHttpStatus(status);
	}

	/**
	 * @param cause
	 */
	public ServiceException(Throwable cause)
	{
		this(STATUS, cause);
	}
	
	protected ServiceException(HttpResponseStatus status, Throwable cause)
	{
		super(cause);
		setHttpStatus(status);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServiceException(String message, Throwable cause)
	{
		this(STATUS, message, cause);
	}

	/**
     * @param internalServerError
     * @param message
     * @param cause
     */
    protected ServiceException(HttpResponseStatus status, String message, Throwable cause)
    {
    	super(message, cause);
    	setHttpStatus(status);
    }

	public HttpResponseStatus getHttpStatus()
    {
    	return httpStatus;
    }
	
	private void setHttpStatus(HttpResponseStatus status)
	{
		this.httpStatus = status;
	}
	
	
	// CONVENIENCE - STATIC

	public static boolean isAssignableFrom(Throwable exception)
    {
	    return ServiceException.class.isAssignableFrom(exception.getClass());
    }

}
