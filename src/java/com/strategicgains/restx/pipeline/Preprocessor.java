/*
 * Copyright 2010, eCollege, Inc.  All rights reserved.
 */
package com.strategicgains.restx.pipeline;

import com.strategicgains.restx.Request;

/**
 * @author toddf
 * @since Aug 31, 2010
 */
public interface Preprocessor
{
	public void process(Request request);
}
