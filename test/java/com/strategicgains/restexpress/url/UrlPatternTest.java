/*
    Copyright 2010, Strategic Gains, Inc.

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
package com.strategicgains.restexpress.url;

import static org.junit.Assert.*;

import org.junit.Test;


/**
 * @author toddf
 * @since Apr 28, 2010
 */
public class UrlPatternTest
{
	private UrlPattern p = new UrlPattern("/xxx/{a_id}/yyy/{b_id}");
	private UrlPattern pFormat = new UrlPattern("/xxx/{a_id}/yyy/{b_id}.{format}");

	@Test
	public void shouldMatchUrlWithFormat()
	{
		assertTrue(pFormat.matches("/xxx/toddf/yyy/joez.json"));
		assertTrue(pFormat.matches("/xxx/12345/yyy/67890.json"));
		assertTrue(pFormat.matches("/xxx/toddf/yyy/joez.xml"));
		assertTrue(pFormat.matches("/xxx/toddf/yyy/joez.json?x=y&a=b"));
		assertTrue(pFormat.matches("/xxx/toddf/yyy/joez"));
		assertTrue(pFormat.matches("/xxx/v1_0_1/yyy/yada"));
		assertTrue(pFormat.matches("/xxx/v1_0_1/yyy/yada.json"));
		assertTrue(pFormat.matches("/xxx/v1_0_1/yyy/yada.json?x=y&a=b"));
	}

	@Test
	public void shouldMatchUrlWithoutFormat()
	{
		assertTrue(p.matches("/xxx/toddf/yyy/joez"));
		assertTrue(p.matches("/xxx/12345/yyy/67890"));
		assertTrue(p.matches("/xxx/toddf/yyy/joez"));
		assertTrue(p.matches("/xxx/toddf/yyy/joez?x=y&a=b"));
	}

	@Test
	public void shouldNotMatchUrlWithFormat()
	{
		assertFalse(pFormat.matches("/xxx/toddf/yyy/joez/"));
		assertFalse(pFormat.matches("/xxx/toddf/yyy/joez."));
		assertFalse(pFormat.matches("/aaa/toddf/yyy/joez.json"));
		assertFalse(pFormat.matches("/xxx/toddf/bbb/joez.json"));
		assertFalse(pFormat.matches("/xxx/toddf/yyy/"));
		assertFalse(pFormat.matches("/xxx/toddf/yyy"));
	}

	@Test
	public void shouldNotMatchUrlWithoutFormat()
	{
		assertFalse(p.matches("/xxx/toddf/yyy/joez/"));
		assertFalse(p.matches("/xxx/toddf/yyy/joez."));
		assertFalse(p.matches("/aaa/toddf/yyy/joez.json"));
		assertFalse(p.matches("/xxx/toddf/bbb/joez.json"));
		assertFalse(p.matches("/xxx/toddf/yyy/"));
		assertFalse(p.matches("/xxx/toddf/yyy"));
	}

	@Test
	public void shouldParseParametersWithFormat()
	{
		UrlMatch match = pFormat.match("/xxx/12345/yyy/67890.json");
		assertNotNull(match);
		assertEquals("json", match.get("format"));
		assertEquals("12345", match.get("a_id"));
		assertEquals("67890", match.get("b_id"));
	}

	@Test
	public void shouldParseParametersWithoutFormat()
	{
		UrlMatch match = p.match("/xxx/12345/yyy/67890");
		assertNotNull(match);
		assertNull(match.get("format"));
		assertEquals("12345", match.get("a_id"));
		assertEquals("67890", match.get("b_id"));
	}

	@Test
	public void shouldParseParametersWithQueryStringAndFormat()
	{
		UrlMatch match = pFormat.match("/xxx/12345/yyy/67890.json?x=y&a=b");
		assertNotNull(match);
		assertEquals("json", match.get("format"));
		assertEquals("12345", match.get("a_id"));
		assertEquals("67890", match.get("b_id"));
	}

	@Test
	public void shouldParseParametersWithQueryStringAndWithoutFormat()
	{
		UrlMatch match = p.match("/xxx/12345/yyy/67890?x=y&a=b");
		assertNotNull(match);
		assertNull(match.get("format"));
		assertEquals("12345", match.get("a_id"));
		assertEquals("67890", match.get("b_id"));
	}

//	@Test
//	public void shouldParseParametersWithPeriods()
//	{
//		UrlMatch match = p.match("/xxx/1.0/yyy/67890?x=y&a=b");
//		assertNotNull(match);
//		assertNull(match.get("format"));
//		assertEquals("1.0", match.get("a_id"));
//		assertEquals("67890", match.get("b_id"));
//	}
	
	@Test
	public void shouldParseTimestampParameters()
	{
		UrlPattern huge = new UrlPattern("/name/{ORDER}/{NO_OF_RECORDS}/{SIFREF_ID}/{REQUEST_TIME_STAMP}/{KEY_MONIKER}/{AUTHENTICATION_TOKEN}");
		UrlMatch match = huge.match("/name/asc/4/2A3B4C5E6D/12345/foo/560261e05f7c21533a3d7d09efff4b91eb7ca702f490e073e1d51614c2e33c1e?contenttype=jsonp%26jsonp=_jqjsp%26_1287161495587");
		assertNotNull(match);
		assertEquals("12345", match.get("REQUEST_TIME_STAMP"));
		
		match = huge.match("/name/asc/4/2A3B4C5E6D/2010-10-15T16:51:33Z/foo/560261e05f7c21533a3d7d09efff4b91eb7ca702f490e073e1d51614c2e33c1e?contenttype=jsonp&jsonp=_jqjsp&_1287161495587=");
		assertNotNull(match);
		assertEquals("2010-10-15T16:51:33Z", match.get("REQUEST_TIME_STAMP"));
		
		match = huge.match("/name/asc/4/2A3B4C5E6D/2010-10-15T16%253A51%253A33Z/foo/560261e05f7c21533a3d7d09efff4b91eb7ca702f490e073e1d51614c2e33c1e?contenttype=jsonp%26jsonp=_jqjsp%26_1287161495587");
		assertNotNull(match);
		assertEquals("2010-10-15T16%253A51%253A33Z", match.get("REQUEST_TIME_STAMP"));
	}
	
	@Test
	public void shouldParseUrlCharactersInParameters()
	{
		UrlMatch match = p.match("/xxx/$-_+*()~/yyy/:,!'%");
		assertNotNull(match);
		assertEquals("$-_+*()~", match.get("a_id"));
		assertEquals(":,!'%", match.get("b_id"));
	}
	
	@Test
	public void shouldReturnBaseUriAsNormalizedUrlPattern()
	{
		assertEquals("/xxx/{a_id}/yyy/{b_id}", new UrlPattern("/xxx/{a_id}/yyy/{b_id}").getNormalizedUrlPattern());
		assertEquals("/xxx/{a_id}/yyy/{b_id}", new UrlPattern("/xxx/{a_id}/yyy/{b_id}.{format}").getNormalizedUrlPattern());
	}
}
