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
package com.strategicgains.restexpress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.strategicgains.restexpress.route.RouteDeclaration;
import com.strategicgains.restexpress.serialization.SerializationProcessor;
import com.strategicgains.restexpress.serialization.json.DefaultJsonProcessor;
import com.strategicgains.restexpress.serialization.xml.DefaultXmlProcessor;


/**
 * @author toddf
 * @since Jan 28, 2011
 */
public class RestExpressTest
{
	private RestExpress server = new RestExpress(new Routes());

	@Test
	public void shouldUseDefaults()
	{
		assertEquals(Format.JSON, server.getDefaultFormat());
		assertTrue(server.getSerializationProcessors().containsKey(Format.JSON));
		assertTrue(server.getSerializationProcessors().containsKey(Format.XML));
		assertEquals(2, server.getSerializationProcessors().size());

		assertEquals(8081, server.getPort());
		assertTrue(server.getMessageObservers().isEmpty());
		assertTrue(server.getPostprocessors().isEmpty());
		assertTrue(server.getPreprocessors().isEmpty());
		assertTrue(server.shouldUseSystemOut());
		assertNotNull(server.getRouteDeclarations());
	}
	
	@Test
	public void shouldDisableJson()
	{
		server.noJson();
		assertEquals(Format.JSON, server.getDefaultFormat());
		assertFalse(server.getSerializationProcessors().containsKey(Format.JSON));
		assertTrue(server.getSerializationProcessors().containsKey(Format.XML));
		assertEquals(1, server.getSerializationProcessors().size());
	}
	
	@Test
	public void shouldDisableXml()
	{
		server.noXml();
		assertEquals(Format.JSON, server.getDefaultFormat());
		assertTrue(server.getSerializationProcessors().containsKey(Format.JSON));
		assertFalse(server.getSerializationProcessors().containsKey(Format.XML));
		assertEquals(1, server.getSerializationProcessors().size());
	}
	
	@Test
	public void shouldMakeXmlDefault()
	{
		server.supportXml(true);
		assertEquals(Format.XML, server.getDefaultFormat());
		assertTrue(server.getSerializationProcessors().containsKey(Format.JSON));
		assertTrue(server.getSerializationProcessors().containsKey(Format.XML));
		assertEquals(2, server.getSerializationProcessors().size());
	}

	@Test
	public void shouldSupportText()
	{
		server.supportTxt();
		assertEquals(Format.JSON, server.getDefaultFormat());
		assertTrue(server.getSerializationProcessors().containsKey(Format.JSON));
		assertTrue(server.getSerializationProcessors().containsKey(Format.XML));
		assertTrue(server.getSerializationProcessors().containsKey(Format.TXT));
		assertEquals(3, server.getSerializationProcessors().size());
	}

	@Test
	public void shouldMakeTextDefault()
	{
		server.supportTxt(true);
		assertEquals(Format.TXT, server.getDefaultFormat());
		assertTrue(server.getSerializationProcessors().containsKey(Format.JSON));
		assertTrue(server.getSerializationProcessors().containsKey(Format.XML));
		assertTrue(server.getSerializationProcessors().containsKey(Format.TXT));
		assertEquals(3, server.getSerializationProcessors().size());
	}

	@Test
	public void shouldCustomizeJsonSerializer()
	{
		server.putSerializationProcessor(Format.JSON, new DefaultJsonProcessor());
		assertEquals(Format.JSON, server.getDefaultFormat());
		assertTrue(server.getSerializationProcessors().containsKey(Format.JSON));
		assertTrue(server.getSerializationProcessors().containsKey(Format.XML));
		assertEquals(2, server.getSerializationProcessors().size());
	}

	@Test
	public void shouldCustomizeXmlSerializer()
	{
		server.putSerializationProcessor(Format.XML, new DefaultXmlProcessor());
		assertEquals(Format.JSON, server.getDefaultFormat());
		assertTrue(server.getSerializationProcessors().containsKey(Format.JSON));
		assertTrue(server.getSerializationProcessors().containsKey(Format.XML));
		assertEquals(2, server.getSerializationProcessors().size());
	}

	@Test
	public void shouldNotUpdateJsonSerializer()
	{
		SerializationProcessor sp = new DefaultJsonProcessor();
		server.putSerializationProcessor(Format.JSON, sp);
		server.supportJson(true);
		assertEquals(Format.JSON, server.getDefaultFormat());
		assertTrue(server.getSerializationProcessors().containsKey(Format.JSON));
		assertTrue(server.getSerializationProcessors().containsKey(Format.XML));
		assertEquals(2, server.getSerializationProcessors().size());
		
		assertTrue(sp == server.getSerializationProcessors().get(Format.JSON));
	}

	@Test
	public void shouldNotUpdateXmlSerializer()
	{
		SerializationProcessor sp = new DefaultXmlProcessor();
		server.putSerializationProcessor(Format.XML, sp);
		server.supportXml(true);
		assertEquals(Format.XML, server.getDefaultFormat());
		assertTrue(server.getSerializationProcessors().containsKey(Format.JSON));
		assertTrue(server.getSerializationProcessors().containsKey(Format.XML));
		assertEquals(2, server.getSerializationProcessors().size());
		
		assertTrue(sp == server.getSerializationProcessors().get(Format.XML));
	}

	@Test
	public void shouldNotUpdateTxtSerializer()
	{
		SerializationProcessor sp = new DefaultXmlProcessor();
		server.putSerializationProcessor(Format.TXT, sp);
		server.supportTxt(true);
		assertEquals(Format.TXT, server.getDefaultFormat());
		assertTrue(server.getSerializationProcessors().containsKey(Format.JSON));
		assertTrue(server.getSerializationProcessors().containsKey(Format.XML));
		assertTrue(server.getSerializationProcessors().containsKey(Format.TXT));
		assertEquals(3, server.getSerializationProcessors().size());
		
		assertTrue(sp == server.getSerializationProcessors().get(Format.TXT));
	}
	
	@Test
	public void shouldNotUseSystemOut()
	{
		server.noSystemOut();
		assertFalse(server.shouldUseSystemOut());
	}

	private class Routes
	extends RouteDeclaration
	{
        @Override
        protected void defineRoutes()
        {
        }
	}
}
