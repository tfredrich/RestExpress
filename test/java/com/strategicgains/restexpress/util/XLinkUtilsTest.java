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

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.strategicgains.restexpress.domain.XLink;
import com.strategicgains.restexpress.domain.XLinkFactory;


/**
 * @author toddf
 * @since Apr 19, 2011
 */
public class XLinkUtilsTest
{
	@Test
	public void shouldCreateXLinks()
	{
		List<String> ids = Arrays.asList("one", "two", "three");
		List<XLink> xlinks = XLinkUtils.asXLinks(ids, "id", "/strings/{id}", (String[]) null);
		assertNotNull(xlinks);
		assertEquals(3, xlinks.size());
		assertEquals("one", xlinks.get(0).id());
		assertEquals("/strings/one", xlinks.get(0).href());
		assertNull(xlinks.get(0).rel());

		assertEquals("two", xlinks.get(1).id());
		assertEquals("/strings/two", xlinks.get(1).href());
		assertNull(xlinks.get(1).rel());

		assertEquals("three", xlinks.get(2).id());
		assertEquals("/strings/three", xlinks.get(2).href());
		assertNull(xlinks.get(2).rel());
	}

	@Test
	public void shouldCreateXLinksUsingPairs()
	{
		List<String> ids = Arrays.asList("one", "two", "three");
		List<XLink> xlinks = XLinkUtils.asXLinks(ids, "id", "/{prefix}/{node}/{id}", "prefix", "strings", "node", "simple");
		assertNotNull(xlinks);
		assertEquals(3, xlinks.size());
		assertEquals("one", xlinks.get(0).id());
		assertEquals("/strings/simple/one", xlinks.get(0).href());
		assertNull(xlinks.get(0).rel());

		assertEquals("two", xlinks.get(1).id());
		assertEquals("/strings/simple/two", xlinks.get(1).href());
		assertNull(xlinks.get(1).rel());

		assertEquals("three", xlinks.get(2).id());
		assertEquals("/strings/simple/three", xlinks.get(2).href());
		assertNull(xlinks.get(2).rel());
	}

	@Test
	public void shouldCreateXLinksUsingXLinkFactory()
	{
		XLinkFactory factory = new RelXLinkFactory();
		List<String> ids = Arrays.asList("one", "two", "three");
		List<XLink> xlinks = XLinkUtils.asXLinks(ids, "id", "/{prefix}/{node}/{id}", factory, "prefix", "strings", "node", "simple");
		assertNotNull(xlinks);
		assertEquals(3, xlinks.size());
		assertEquals("one", xlinks.get(0).id());
		assertEquals("/strings/simple/one", xlinks.get(0).href());
		assertEquals("relationValue", xlinks.get(0).rel());

		assertEquals("two", xlinks.get(1).id());
		assertEquals("/strings/simple/two", xlinks.get(1).href());
		assertEquals("relationValue", xlinks.get(1).rel());

		assertEquals("three", xlinks.get(2).id());
		assertEquals("/strings/simple/three", xlinks.get(2).href());
		assertEquals("relationValue", xlinks.get(2).rel());
	}
	
	private class RelXLinkFactory
	implements XLinkFactory
	{
        @Override
        public XLink create(String id, String href)
        {
        	return new XLink(id, "relationValue", href);
        }
	}
}
