/*
 * Copyright 2010, eCollege, Inc.  All rights reserved.
 */
package com.strategicgains.restexpress.pipeline;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;

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
