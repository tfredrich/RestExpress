/*
 * Copyright 2010, eCollege, Inc.  All rights reserved.
 */
package com.strategicgains.restx.pipeline;

import com.strategicgains.restx.Request;
import com.strategicgains.restx.Response;

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
