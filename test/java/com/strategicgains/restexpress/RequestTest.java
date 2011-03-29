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

import static org.junit.Assert.*;

import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.junit.Before;
import org.junit.Test;

import com.strategicgains.restexpress.exception.BadRequestException;

/**
 * @author toddf
 * @since Mar 29, 2011
 */
public class RequestTest
{
	private Request request;

	@Before
	public void initialize()
	{
		request = new Request(new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/foo?param1=bar&param2=blah"), null);
	}
	
	@Test
	public void shouldApplyQueryStringParamsAsHeaders()
	{
		assertEquals("bar", request.getRawHeader("param1"));
		assertEquals("blah", request.getRawHeader("param2"));
	}

	@Test
	public void shouldSetAndGetHeader()
	{
		String key = "header-key";
		String value = "header value";
		request.addHeader(key, value);
		assertEquals(value, request.getRawHeader(key));
	}
	
	@Test
	public void shouldUrlDecodeHeader()
	{
		String key = "validUrlDecode";
		String value = "%20this%20that";
		request.addHeader(key, value);
		assertEquals(" this that", request.getUrlDecodedHeader(key));
	}

	@Test(expected=BadRequestException.class)
	public void shouldThrowBadRequestExceptionOnInvalidUrlDecodeHeader()
	{
		String key = "invalidUrlDecode";
		String value = "%invalid";
		request.addHeader(key, value);
		request.getUrlDecodedHeader(key);
	}

	@Test(expected=BadRequestException.class)
	public void shouldThrowBadRequestExceptionOnMissingHeader()
	{
		request.getRawHeader("missing", "missing header");
	}
	
	@Test
	public void shouldBeGetRequest()
	{
		assertEquals(HttpMethod.GET, request.getMethod());
		assertEquals(HttpMethod.GET, request.getEffectiveHttpMethod());
	}
	
	@Test
	public void shouldBePostRequest()
	{
		Request postRequest = new Request(new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/foo"), null);
		assertEquals(HttpMethod.POST, postRequest.getMethod());
		assertEquals(HttpMethod.POST, postRequest.getEffectiveHttpMethod());
	}

	@Test
	public void shouldBePutRequest()
	{
		Request putRequest = new Request(new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PUT, "/foo"), null);
		assertEquals(HttpMethod.PUT, putRequest.getMethod());
		assertEquals(HttpMethod.PUT, putRequest.getEffectiveHttpMethod());
	}
	
	@Test
	public void shouldBeDeleteRequest()
	{
		Request deleteRequest = new Request(new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.DELETE, "/foo"), null);
		assertEquals(HttpMethod.DELETE, deleteRequest.getMethod());
		assertEquals(HttpMethod.DELETE, deleteRequest.getEffectiveHttpMethod());
	}

	@Test
	public void shouldBeEffectivePutRequest()
	{
		Request putRequest = new Request(new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/foo?_method=pUt"), null);
		assertEquals(HttpMethod.POST, putRequest.getMethod());
		assertEquals(HttpMethod.PUT, putRequest.getEffectiveHttpMethod());
	}

	@Test
	public void shouldBeEffectiveDeleteRequest()
	{
		Request deleteRequest = new Request(new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/foo?_method=DeLeTe"), null);
		assertEquals(HttpMethod.POST, deleteRequest.getMethod());
		assertEquals(HttpMethod.DELETE, deleteRequest.getEffectiveHttpMethod());
	}

	@Test
	public void shouldBeEffectivePostRequest()
	{
		Request deleteRequest = new Request(new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/foo?_method=xyzt"), null);
		assertEquals(HttpMethod.POST, deleteRequest.getMethod());
		assertEquals(HttpMethod.POST, deleteRequest.getEffectiveHttpMethod());
	}
}
