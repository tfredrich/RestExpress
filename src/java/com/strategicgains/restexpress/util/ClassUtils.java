/*
    Copyright 2008, Strategic Gains, Inc.

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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.strategicgains.jbel.exception.FunctionException;
import com.strategicgains.jbel.function.UnaryFunction;

/**
 * @author toddf
 * @since Aug 18, 2008
 */
public class ClassUtils
{
	// SECTION: CONSTANTS

	public static final int IGNORED_FIELD_MODIFIERS = Modifier.FINAL | Modifier.STATIC | Modifier.TRANSIENT;

	
	// SECTION: CONSTRUCTOR - PRIVATE
	
	private ClassUtils()
	{
		// Prevents instantiation.
	}

	
	// SECTION: CLASS UTILITIES

	/**
	 * Traverses from the given object up the inheritance hierarchy to list all the
	 * declared fields.
	 * 
	 * @param object
	 * @return
	 */
	public static List<Field> getAllDeclaredFields(Class<?> aClass)
	{
		return getAllDeclaredFields(aClass, IGNORED_FIELD_MODIFIERS);
	}

	public static List<Field> getAllDeclaredFields(Class<?> aClass, int modifiers)
	{
		FieldListClosure closure = new ClassUtils.FieldListClosure(new ArrayList<Field>(), modifiers);
		getAllDeclaredFields(aClass, closure);
		return closure.getValues();
	}
    
    public static HashMap<String, Field> getAllDeclaredFieldsByName(Class<?> aClass)
    {
    	return getAllDeclaredFieldsByName(aClass, IGNORED_FIELD_MODIFIERS);
    }

    public static HashMap<String, Field> getAllDeclaredFieldsByName(Class<?> aClass, int modifiers)
    {
		FieldHashMapClosure closure = new ClassUtils.FieldHashMapClosure(new HashMap<String, Field>(), modifiers);
		getAllDeclaredFields(aClass, closure);
		return closure.getValues();
    }

    
    // SECTION: UTILITY - PRIVATE

    private static void getAllDeclaredFields(Class<?> aClass, UnaryFunction function)
    {
    	for (Field field : aClass.getDeclaredFields())
    	{
    		try
            {
	            function.perform(field);
            }
            catch (FunctionException e)
            {
	            e.printStackTrace();
            }
    	}

    	if (aClass.getSuperclass() != null)
		{
			getAllDeclaredFields(aClass.getSuperclass(), function);
		}
    }
    

    // SECTION: INNER CLASSES
    
    private static class FieldListClosure
    implements UnaryFunction
    {
    	private List<Field> values;
    	private int ignoredModifiers;

    	public FieldListClosure(List<Field> values, int modifiers)
    	{
    		super();
    		this.values = values;
    		this.ignoredModifiers = modifiers;
    	}

        @Override
        public Object perform(Object argument)
        throws FunctionException
        {
        	Field field = (Field) argument;

        	if ((field.getModifiers() & ignoredModifiers) == 0)
        	{
        		values.add((Field) argument);
        	}

	        return null;
        }
        
        public List<Field> getValues()
        {
        	return values;
        }
    }
    
    private static class FieldHashMapClosure
    implements UnaryFunction
    {
    	private HashMap<String, Field> values;
    	private int ignoredModifiers;

    	public FieldHashMapClosure(HashMap<String, Field> values, int modifiers)
    	{
    		super();
    		this.values = values;
    		this.ignoredModifiers = modifiers;
    	}

        @Override
        public Object perform(Object argument)
        throws FunctionException
        {
        	Field field = (Field) argument;

        	if ((field.getModifiers() & ignoredModifiers) == 0)
        	{
        		values.put(field.getName(), field);
        	}

	        return null;
        }
        
        public HashMap<String, Field> getValues()
        {
        	return values;
        }
    }
}
