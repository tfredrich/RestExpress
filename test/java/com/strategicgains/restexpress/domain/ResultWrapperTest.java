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
package com.strategicgains.restexpress.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.exception.ServiceException;


/**
 * @author toddf
 * @since May 11, 2011
 */
public class ResultWrapperTest
{
	private Response response = new Response();

	@Test
	public void shouldHandleCheckedException()
	{
		response.setException(new IOException("An IOException was thrown"));
		response.setResponseCode(1);
		ResultWrapper w = ResultWrapper.fromResponse(response);
		assertNotNull(w);
		assertEquals("fail", w.getStatus());
		assertEquals(1, w.getCode());
		assertEquals("An IOException was thrown", w.getMessage());
		assertEquals(IOException.class.getSimpleName(), w.getData());
	}

	@Test
	public void shouldHandleUncheckedException()
	{
		response.setException(new ArrayIndexOutOfBoundsException("An ArrayIndexOutOfBoundsException was thrown"));
		response.setResponseCode(2);
		ResultWrapper w = ResultWrapper.fromResponse(response);
		assertNotNull(w);
		assertEquals("fail", w.getStatus());
		assertEquals(2, w.getCode());
		assertEquals("An ArrayIndexOutOfBoundsException was thrown", w.getMessage());
		assertEquals(ArrayIndexOutOfBoundsException.class.getSimpleName(), w.getData());
	}

	@Test
	public void shouldHandleServiceException()
	{
		response.setException(new ServiceException("A ServiceException was thrown"));
		response.setResponseCode(3);
		ResultWrapper w = ResultWrapper.fromResponse(response);
		assertNotNull(w);
		assertEquals("error", w.getStatus());
		assertEquals(3, w.getCode());
		assertEquals("A ServiceException was thrown", w.getMessage());
		assertEquals(ServiceException.class.getSimpleName(), w.getData());
	}

	@Test
	public void shouldHandleRaw100Code()
	{
		response.setResponseCode(100);
		response.setBody("Success Body");
		ResultWrapper w = ResultWrapper.fromResponse(response);
		assertNotNull(w);
		assertEquals("success", w.getStatus());
		assertEquals(100, w.getCode());
		assertNull(w.getMessage());
		assertEquals("Success Body", w.getData());
	}

	@Test
	public void shouldHandleRaw400Code()
	{
		response.setResponseCode(400);
		response.setBody("Error Body");
		ResultWrapper w = ResultWrapper.fromResponse(response);
		assertNotNull(w);
		assertEquals("error", w.getStatus());
		assertEquals(400, w.getCode());
		assertNull(w.getMessage());
		assertEquals("Error Body", w.getData());
	}

	@Test
	public void shouldHandleRaw500Code()
	{
		response.setResponseCode(500);
		response.setBody("Fail Body");
		ResultWrapper w = ResultWrapper.fromResponse(response);
		assertNotNull(w);
		assertEquals("fail", w.getStatus());
		assertEquals(500, w.getCode());
		assertNull(w.getMessage());
		assertEquals("Fail Body", w.getData());
	}

	@Test
	public void shouldHandleSuccess()
	{
		response.setBody("Success Body");
		ResultWrapper w = ResultWrapper.fromResponse(response);
		assertNotNull(w);
		assertEquals("success", w.getStatus());
		assertEquals(200, w.getCode());
		assertNull(w.getMessage());
		assertEquals("Success Body", w.getData());
	}

}
