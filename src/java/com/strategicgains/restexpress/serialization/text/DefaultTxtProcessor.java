/*
 * Copyright 2010, Strategic Gains, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.strategicgains.restexpress.serialization.text;

import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;

import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.serialization.SerializationProcessor;
import com.strategicgains.restexpress.serialization.Serializer;

/**
 * This SerializationProcessor implementation performs serialize() only so it's applicable
 * for GET-only routes, or routes where no deserialization of the request body is expected.
 * 
 * This implementation simply utilizes toString() for serialization.  Thus, it is recommended
 * that domain objects being serialized override the toString() method.
 * 
 * If deserialization is attempted, an UnsupportedOperationException is thrown.
 * 
 * @author toddf
 * @since Oct 6, 2010
 */
public class DefaultTxtProcessor
implements SerializationProcessor
{
	private Map<Class<?>, TextSerializer> aliases = new HashMap<Class<?>, TextSerializer>();

	public DefaultTxtProcessor()
	{
		super();
	}

	/**
	 * Essentially override the toString() method for a given Class with a TextSerializer.
	 * Observes the inheritance hierarchy such that if an alias is not given for the sub-class
	 * but is provided for the superclass, the superclass's alias will be used to serialize
	 * the object.
	 * 
	 * @param aClass
	 * @param serializer
	 */
	public void alias(Class<?> aClass, TextSerializer serializer)
	{
		if (!aliases.containsKey(aClass))
		{
			aliases.put(aClass, serializer);
		}
	}

	@Override
	public String serialize(Object object)
	{
		Serializer serializer = findSerializerFor(object);
		return (serializer == null ? String.valueOf(object) : serializer.serialize(object));
	}

	private TextSerializer findSerializerFor(Object object)
    {
		Class<?> aClass = object.getClass();
		TextSerializer serializer = null;
		
		while (serializer == null && aClass != null)
		{
			serializer = aliases.get(aClass);
			
			if (serializer == null)
			{
				aClass = aClass.getSuperclass();
			}
		}
		
	    return serializer;
    }

	@Override
	public <T> T deserialize(String text, Class<T> type)
	{
		throw new UnsupportedOperationException("Text processor cannot perform deserialization");
	}

	@Override
	public <T> T deserialize(ChannelBuffer text, Class<T> type)
	{
		throw new UnsupportedOperationException("Text processor cannot perform deserialization");
	}

	@Override
	public String getResultingContentType()
	{
		return RestExpress.CONTENT_TYPE_TEXT_PLAIN;
	}
}
