/*
 * Copyright 2010, eCollege, Inc.  All rights reserved.
 */
package com.strategicgains.restexpress.pipeline;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;

/**
 * Defines the interface for processing that happens after the service is called but
 * before the response is returned.  Postprocessors do not get called in an exception
 * case.  Furuthermore, if a Postprocessor throws an exception, the rest of the 
 * Postprocessors in the chain are skipped.
 * 
 * @author toddf
 * @since Aug 31, 2010
 */
public interface Postprocessor
{
	public void process(Request request, Response response);
}
