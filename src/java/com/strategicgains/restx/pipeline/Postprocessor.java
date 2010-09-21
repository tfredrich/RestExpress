/*
 * Copyright 2010, eCollege, Inc.  All rights reserved.
 */
package com.strategicgains.restx.pipeline;

import com.strategicgains.restx.Request;
import com.strategicgains.restx.Response;

/**
 * Defines the interface for processing that happens after the service is called but
 * before the response is returned.
 * 
 * @author toddf
 * @since Aug 31, 2010
 */
public interface Postprocessor
{
	public void process(Request request, Response response);
}
