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
package com.strategicgains.restexpress.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;


/**
 * @author toddf
 * @since Apr 8, 2011
 */
public class ExceptionUtilsTest
{
	@Test
	public void shouldHandleNull()
	{
		assertNull(ExceptionUtils.findRootCause(null));
	}
	
	@Test
	public void shouldReturnSame()
	{
		Throwable t = new NullPointerException();
		assertEquals(t, ExceptionUtils.findRootCause(t));
	}
	
	@Test
	public void shouldReturnRoot()
	{
		Throwable npe = new NullPointerException("Manually-thrown NullPointerException");
		Throwable t = new ServiceException(new Exception(npe));
		assertEquals(npe, ExceptionUtils.findRootCause(t));
	}
}
