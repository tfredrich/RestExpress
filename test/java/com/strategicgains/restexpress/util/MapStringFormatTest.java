/*
 * Copyright 2008, eCollege, Inc.  All rights reserved.
 */
package com.strategicgains.restexpress.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @author toddf
 * @since Nov 24, 2008
 */
public class MapStringFormatTest
{
	@Test
	public void shouldFormatWithDefaultDelimiters()
	{
		MapStringFormat formatter = new MapStringFormat();
		String template = "{last_name}, {first_name} {middle_initial}.";
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("first_name", "Todd");
		parameters.put("middle_initial", "A");
		parameters.put("last_name", "Fredrich");
		String result = formatter.format(template, parameters);
		assertEquals("Fredrich, Todd A.", result);
	}

	@Test
	public void shouldFormatWithStringArray()
	{
		MapStringFormat formatter = new MapStringFormat();
		String template = "{last_name}, {first_name} {middle_initial}.";
		String result = formatter.format(template, "first_name", "Todd", "middle_initial", "A", "last_name", "Fredrich");
		assertEquals("Fredrich, Todd A.", result);
	}

	@Test(expected=IllegalArgumentException.class)
	public void shouldThrowExceptionWithUnbalancedParameters()
	{
		MapStringFormat formatter = new MapStringFormat();
		String template = "{last_name}, {first_name} {middle_initial}.";
		formatter.format(template, "first_name", "Todd", "middle_initial", "last_name", "Fredrich");
		fail("Shouldn't get here");
	}

	@Test
	public void shouldFormatWithSpecifiedDelimiters()
	{
		MapStringFormat formatter = new MapStringFormat("[", "]");
		String template = "[last_name], [first_name] [middle_initial].";
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("first_name", "Todd");
		parameters.put("middle_initial", "A");
		parameters.put("last_name", "Fredrich");
		String result = formatter.format(template, parameters);
		assertEquals("Fredrich, Todd A.", result);
	}

	@Test
	public void shouldFormatWithStringDelimiters()
	{
		MapStringFormat formatter = new MapStringFormat("<start>", "<end>");
		String template = "<start>last_name<end>, <start>first_name<end> <start>middle_initial<end>.";
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("first_name", "Todd");
		parameters.put("middle_initial", "A");
		parameters.put("last_name", "Fredrich");
		String result = formatter.format(template, parameters);
		assertEquals("Fredrich, Todd A.", result);
	}
}
