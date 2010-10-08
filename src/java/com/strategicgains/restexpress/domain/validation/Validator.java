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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.strategicgains.jbel.exception.FunctionException;
import com.strategicgains.jbel.function.UnaryFunction;
import com.strategicgains.restexpress.util.ClassUtils;

/**
 * @author toddf
 * @since Oct 7, 2010
 */
public class Validator
{
	private Validator()
	{
		// Prevents instantiation.
	}

	public static List<String> validate(Object object)
	{
		List<String> errors = new ArrayList<String>();

		validateFields(object, errors);

		return errors;
	}

	private static void validateFields(Object object, List<String> errors)
	{
		Collection<Field> fields = ClassUtils.getAllDeclaredFields(object.getClass());
		FieldValidationClosure validation = new FieldValidationClosure(object, errors);

		for (Field field : fields)
		{
			if (shouldValidated(field))
			{
				try
                {
	                validation.perform(field);
                }
                catch (FunctionException e)
                {
	                e.printStackTrace();
                }
			}
		}
	}

	private static String determineName(Validate annotation, Field field)
	{
		return (annotation.name().isEmpty() ? field.getName() : annotation.name());
	}

	private static boolean shouldValidated(Field field)
	{
		return field.isAnnotationPresent(Validate.class);
	}
	
	private static class FieldValidationClosure
	implements UnaryFunction
	{
		private Object object;
		private List<String> errors;
		
		public FieldValidationClosure(Object object, List<String> errors)
		{
			super();
			this.errors = errors;
			this.object = object;
		}

        @Override
        public Object perform(Object argument)
        throws FunctionException
        {
        	Field field = (Field) argument;
        	field.setAccessible(true);
        	Validate annotation = field.getAnnotation(Validate.class);
        	String name = determineName(annotation, field);
        	Object value;
            try
            {
	            value = field.get(object);
            }
            catch (Exception e)
            {
            	throw new FunctionException(e);
            }
        	
        	if (annotation.greaterThan() != Integer.MAX_VALUE)
        	{
        		int intValue = ((Integer) value).intValue();
        		Validations.greaterThan(name, intValue, annotation.greaterThan(), errors);
        	}
        	
        	if (annotation.lessThan() != Integer.MIN_VALUE)
        	{
        		int intValue = ((Integer) value).intValue();
        		Validations.lessThan(name, intValue, annotation.lessThan(), errors);
        	}
        	
        	if (annotation.required())
        	{
        		String stringValue = (value == null ? null : String.valueOf(value));
            	Validations.require(name, stringValue, errors);
        	}
        	
        	if (annotation.maxLength() > 0)
        	{
        		String stringValue = (value == null ? null : String.valueOf(value));
            	Validations.maxLength(name, stringValue, annotation.maxLength(), errors);
        	}

	        return null;
        }
	}
}
