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

import org.jboss.netty.buffer.ChannelBuffer;

import com.strategicgains.restexpress.serialization.SerializationProcessor;

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
	public DefaultTxtProcessor()
	{
		super();
	}

	@Override
	public String serialize(Object object)
	{
		return object.toString();
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
		return "text/plain";
	}
}
