/*
 * Copyright 2010, eCollege, Inc.  All rights reserved.
 */
package com.strategicgains.restexpress.pipeline;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;


/**
 * Defines the interface for request handlers that 
 * @author toddf
 * @since Aug 31, 2010
 */
public interface PostprocessorAware
{
	public void addPostprocessor(Postprocessor processor);
	public void invokePostprocessors(Request request, Response response);
}
