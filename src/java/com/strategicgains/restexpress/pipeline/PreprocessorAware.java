/*
 * Copyright 2010, eCollege, Inc.  All rights reserved.
 */
package com.strategicgains.restexpress.pipeline;

import com.strategicgains.restexpress.Request;

/**
 * @author toddf
 * @since Aug 31, 2010
 */
public interface PreprocessorAware
{
	public void addPreprocessor(Preprocessor processor);
	public void invokePreprocessors(Request request);
}
