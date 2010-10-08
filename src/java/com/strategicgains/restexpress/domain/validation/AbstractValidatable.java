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
package com.strategicgains.restexpress.domain.validation;

import java.util.List;

import com.strategicgains.restexpress.exception.ValidationException;

/**
 * @author toddf
 * @since Oct 8, 2010
 */
public abstract class AbstractValidatable
implements Validatable
{
	/**
	 * Validate the object properties. Properties (fields) annotated with Validate
	 * are checked.  If validation fails, a ValidationException is thrown which
	 * contains a List of error messages--which are the cause(s) of validation failure.
	 */
	@Override
	public void validate()
	{
		List<String> errors = Validator.validate(this);
		validateInto(errors);
		
		if (!errors.isEmpty())
		{
			throw new ValidationException(errors);
		}
	}
	
	/**
	 * Sub-classes can insert additional validations here by overriding.
	 * 
	 * @param errors the List of errors to accumulate.
	 */
	protected void validateInto(List<String> errors)
	{
		// sub-classes can insert additional validations here by overriding.
	}
}
