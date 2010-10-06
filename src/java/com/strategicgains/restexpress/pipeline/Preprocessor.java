/*
 * Copyright 2010, eCollege, Inc.  All rights reserved.
 */
package com.strategicgains.restexpress.pipeline;

import com.strategicgains.restexpress.Request;


/**
 * @author toddf
 * @since Aug 31, 2010
 */
public interface Preprocessor
{
	public void process(Request request);
}
