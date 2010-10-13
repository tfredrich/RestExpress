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
package com.strategicgains.restexpress.exception;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author toddf
 * @since Oct 13, 2010
 */
public class ExceptionMapping
{
	private Map<Class<?>, Class<?>> exceptions = new HashMap<Class<?>, Class<?>>();
	
	public <T extends Exception, U extends ServiceException> void map(Class<T> inExceptionClass, Class<U> outExceptionClass)
	{
		exceptions.put(inExceptionClass, outExceptionClass);
	}

	public ServiceException getExceptionFor(Exception e)
	{
		Class<?> mapped = exceptions.get(e.getClass());
		
		if (mapped != null)
		{
			try
            {
	            Constructor<?> constructor = mapped.getConstructor(Throwable.class);
	            return (ServiceException) constructor.newInstance(e);
            }
            catch (Exception e1)
            {
	            e1.printStackTrace();
            }
		}
		
		return null;
	}
}
