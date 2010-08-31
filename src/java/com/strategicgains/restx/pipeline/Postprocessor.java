/*
 * Copyright 2010, eCollege, Inc.  All rights reserved.
 */
package com.strategicgains.restx.pipeline;

import com.strategicgains.restx.Request;
import com.strategicgains.restx.Response;

/**
 * @author toddf
 * @since Aug 31, 2010
 */
public interface Postprocessor
{
	public void process(Request request, Response response);
}
