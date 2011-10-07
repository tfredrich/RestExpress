/*
    Copyright 2011, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package com.strategicgains.restexpress.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.strategicgains.restexpress.util.StringUtils.QueryStringCallback;

/**
 * @author toddf
 * @since Oct 7, 2011
 */
public class StringUtilsTest
{
	private int size;

	@Before
	public void setup()
	{
		size = 0;
	}

	@Test
	public void shouldParseQueryStringIntoMap()
	{
		Map<String, String> results = StringUtils.parseQueryString("a=1&b=two&c");
		assertEquals(3, results.size());
		assertEquals("1", results.get("a"));
		assertEquals("two", results.get("b"));
		assertEquals("", results.get("c"));
	}

	@Test
	public void shouldParseNullQueryString()
	{
		Map<String, String> results = StringUtils.parseQueryString(null);
		assertTrue(results.isEmpty());
	}

	@Test
	public void shouldParseEmptyQueryString()
	{
		Map<String, String> results = StringUtils.parseQueryString("");
		assertTrue(results.isEmpty());
	}

	@Test
	public void shouldIterateQueryString()
	{
		StringUtils.iterateQueryString("a=1&b=two&c&bc", 
			new QueryStringCallback()
			{
				private List<String> handled = new ArrayList<String>();

				@Override
				public void assign(String key, String value)
				{
					++size;
					
					if (handled.contains(key))
					{
						fail("already handled key: " + key);
					}
				}
			});

		assertEquals(4, size);
	}

	@Test
	public void shouldIterateNullQueryString()
	{
		StringUtils.iterateQueryString(null, 
			new QueryStringCallback()
			{
				@Override
				public void assign(String key, String value)
				{
					++size;
				}
			});

		assertEquals(0, size);
	}

	@Test
	public void shouldIterateEmptyQueryString()
	{
		StringUtils.iterateQueryString(null, 
			new QueryStringCallback()
			{
				@Override
				public void assign(String key, String value)
				{
					++size;
				}
			});

		assertEquals(0, size);
	}
}
