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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.junit.Before;
import org.junit.Test;

import com.strategicgains.restexpress.ContentType;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.exception.HttpSpecificationException;

/**
 * @author toddf
 * @since Mar 4, 2011
 */
public class HttpSpecificationTest
{
	private Response response;

	@Before
	public void setUp()
	{
		response = new Response();
	}

	@Test
	public void shouldPassOn200()
	{
		response.setResponseStatus(HttpResponseStatus.OK);
		response.setBody("Should be allowed.");
		response.setContentType(ContentType.JSON);
		response.addHeader(HttpHeaders.Names.CONTENT_LENGTH, "15");
		HttpSpecification.enforce(response);
	}

	@Test
	public void shouldPassOn201()
	{
		response.setResponseStatus(HttpResponseStatus.CREATED);
		response.setBody("Should be allowed.");
		response.setContentType(ContentType.JSON);
		response.addHeader(HttpHeaders.Names.CONTENT_LENGTH, "15");
		HttpSpecification.enforce(response);
	}

	@Test
	public void shouldPassOn409()
	{
		response.setResponseStatus(HttpResponseStatus.CONFLICT);
		response.setBody("Should be allowed.");
		response.setContentType(ContentType.JSON);
		response.addHeader(HttpHeaders.Names.CONTENT_LENGTH, "15");
		HttpSpecification.enforce(response);
	}

	@Test
	public void shouldPassOn500()
	{
		response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		response.setBody("Should be allowed.");
		response.setContentType(ContentType.JSON);
		response.addHeader(HttpHeaders.Names.CONTENT_LENGTH, "15");
		HttpSpecification.enforce(response);
	}

	@Test
	public void shouldPassOn100WithoutBody()
	{
		response.setResponseStatus(HttpResponseStatus.CONTINUE);
		response.setBody(null);
		HttpSpecification.enforce(response);
	}

	@Test(expected=HttpSpecificationException.class)
	public void shouldThrowExceptionOn100WithBody()
	{
		response.setResponseStatus(HttpResponseStatus.CONTINUE);
		response.setBody("Should not be allowed.");
		HttpSpecification.enforce(response);
	}

	@Test(expected=HttpSpecificationException.class)
	public void shouldThrowExceptionOn100WithContentType()
	{
		response.setResponseStatus(HttpResponseStatus.CONTINUE);
		response.addHeader(HttpHeaders.Names.CONTENT_TYPE, ContentType.XML);
		HttpSpecification.enforce(response);
	}

	@Test(expected=HttpSpecificationException.class)
	public void shouldThrowExceptionOn100WithContentLength()
	{
		response.setResponseStatus(HttpResponseStatus.CONTINUE);
		response.addHeader(HttpHeaders.Names.CONTENT_LENGTH, "25");
		HttpSpecification.enforce(response);
	}

	@Test
	public void shouldPassOn204WithoutBody()
	{
		response.setResponseStatus(HttpResponseStatus.NO_CONTENT);
		response.setBody(null);
		HttpSpecification.enforce(response);
	}

	@Test(expected=HttpSpecificationException.class)
	public void shouldThrowExceptionOn204WithBody()
	{
		response.setResponseStatus(HttpResponseStatus.NO_CONTENT);
		response.setBody("Should not be allowed.");
		HttpSpecification.enforce(response);
	}

	@Test(expected=HttpSpecificationException.class)
	public void shouldThrowExceptionOn204WithContentType()
	{
		response.setResponseStatus(HttpResponseStatus.NO_CONTENT);
		response.addHeader(HttpHeaders.Names.CONTENT_TYPE, ContentType.XML);
		HttpSpecification.enforce(response);
	}

	@Test(expected=HttpSpecificationException.class)
	public void shouldThrowExceptionOn204WithContentLength()
	{
		response.setResponseStatus(HttpResponseStatus.NO_CONTENT);
		response.addHeader(HttpHeaders.Names.CONTENT_LENGTH, "25");
		HttpSpecification.enforce(response);
	}

	@Test
	public void shouldPassOn304WithoutBody()
	{
		response.setResponseStatus(HttpResponseStatus.NOT_MODIFIED);
		response.setBody(null);
		HttpSpecification.enforce(response);
	}

	@Test(expected=HttpSpecificationException.class)
	public void shouldThrowExceptionOn304WithBody()
	{
		response.setResponseStatus(HttpResponseStatus.NOT_MODIFIED);
		response.setBody("Should not be allowed.");
		HttpSpecification.enforce(response);
	}

	@Test(expected=HttpSpecificationException.class)
	public void shouldThrowExceptionOn304WithContentType()
	{
		response.setResponseStatus(HttpResponseStatus.NOT_MODIFIED);
		response.addHeader(HttpHeaders.Names.CONTENT_TYPE, ContentType.XML);
		HttpSpecification.enforce(response);
	}

	@Test(expected=HttpSpecificationException.class)
	public void shouldThrowExceptionOn304WithContentLength()
	{
		response.setResponseStatus(HttpResponseStatus.NOT_MODIFIED);
		response.addHeader(HttpHeaders.Names.CONTENT_LENGTH, "25");
		HttpSpecification.enforce(response);
	}

	@Test
	public void shouldAllowContentType()
	{
		response.setResponseStatus(HttpResponseStatus.OK);
		assertTrue(HttpSpecification.isContentTypeAllowed(response));
		response.setResponseStatus(HttpResponseStatus.CREATED);
		assertTrue(HttpSpecification.isContentTypeAllowed(response));
		response.setResponseStatus(HttpResponseStatus.CONFLICT);
		assertTrue(HttpSpecification.isContentTypeAllowed(response));
		response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		assertTrue(HttpSpecification.isContentTypeAllowed(response));
	}

	@Test
	public void shouldNotAllowContentType()
	{
		response.setResponseStatus(HttpResponseStatus.CONTINUE);
		assertFalse(HttpSpecification.isContentTypeAllowed(response));
		response.setResponseStatus(HttpResponseStatus.NO_CONTENT);
		assertFalse(HttpSpecification.isContentTypeAllowed(response));
		response.setResponseStatus(HttpResponseStatus.NOT_MODIFIED);
		assertFalse(HttpSpecification.isContentTypeAllowed(response));
	}

	@Test
	public void shouldAllowContentLength()
	{
		response.setResponseStatus(HttpResponseStatus.OK);
		assertTrue(HttpSpecification.isContentLengthAllowed(response));
		response.setResponseStatus(HttpResponseStatus.CREATED);
		assertTrue(HttpSpecification.isContentLengthAllowed(response));
		response.setResponseStatus(HttpResponseStatus.CONFLICT);
		assertTrue(HttpSpecification.isContentLengthAllowed(response));
		response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		assertTrue(HttpSpecification.isContentLengthAllowed(response));
	}

	@Test
	public void shouldNotAllowContentLength()
	{
		response.setResponseStatus(HttpResponseStatus.CONTINUE);
		assertFalse(HttpSpecification.isContentLengthAllowed(response));
		response.setResponseStatus(HttpResponseStatus.NO_CONTENT);
		assertFalse(HttpSpecification.isContentLengthAllowed(response));
		response.setResponseStatus(HttpResponseStatus.NOT_MODIFIED);
		assertFalse(HttpSpecification.isContentLengthAllowed(response));
	}
}
