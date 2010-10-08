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
package com.strategicgains.restexpress.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.strategicgains.restexpress.domain.validation.Validate;
import com.strategicgains.restexpress.domain.validation.Validator;


/**
 * @author toddf
 * @since Oct 8, 2010
 */
public class ValidatorTest
{
	private Inner object = new Inner();

	@Test
	public void shouldFailRequiredValidatedString()
	{
		object.setValidatedInt(7);
		List<String> errors = Validator.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("required"));
		assertTrue(errors.get(0).contains("validated string"));
	}

	@Test
	public void shouldFailMaxLength()
	{
		object.setValidatedInt(7);
		object.setValidatedString("todd was here once, at least");
		List<String> errors = Validator.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("validated string"));
		assertTrue(errors.get(0).contains("5 characters"));
	}

	@Test
	public void shouldFailGreaterThanTest()
	{
		List<String> errors = Validator.validate(object);
		assertEquals(2, errors.size());
		assertTrue(errors.get(1).contains("greater-than 5"));
		assertTrue(errors.get(1).contains("validated integer"));
	}

	@Test
	public void shouldFailLessThanTest()
	{
		object.setValidatedString("todd");
		object.setValidatedInt(25);
		List<String> errors = Validator.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("less-than 10"));
		assertTrue(errors.get(0).contains("validated integer"));
	}
	
	@Test
	public void shouldUseActualPropertyName()
	{
		object.setIntField(5);
		object.setValidatedInt(7);
		object.setValidatedString("todd");
		List<String> errors = Validator.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("intField"));
	}

	@Test
	public void shouldPassValidation()
	{
		object.setValidatedInt(7);
		object.setValidatedString("todd");
		List<String> errors = Validator.validate(object);
		assertTrue(errors.isEmpty());
	}
	
	private class Inner
	{
		@Validate(name="validated string", required=true, maxLength=5)
		private String validatedString;
		private String ignoredString;
		
		@Validate(name="validated integer", greaterThan=5, lessThan=10)
		private int validatedInt;
		private int ignoredInt;
		
		@Validate(greaterThan=-1, lessThan=1)
		private int intField;

		public void setValidatedString(String validatedString)
        {
        	this.validatedString = validatedString;
        }
		
		public void setIgnoredString(String ignoredString)
        {
        	this.ignoredString = ignoredString;
        }
		
		public void setValidatedInt(int validatedInt)
        {
        	this.validatedInt = validatedInt;
        }
		
		public void setIgnoredInt(int ignoredInt)
        {
        	this.ignoredInt = ignoredInt;
        }

		public void setIntField(int intField)
        {
        	this.intField = intField;
        }
	}
}
