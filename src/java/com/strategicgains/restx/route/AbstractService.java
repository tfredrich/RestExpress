/*
 * Copyright 2009, Strategic Gains, Inc.
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

package com.strategicgains.restx.route;

import java.util.Map;

import com.strategicgains.restx.serialization.Deserializer;
import com.strategicgains.restx.serialization.Serializer;

/**
 * @author toddf
 * @since Nov 20, 2009
 */
public abstract class AbstractService
implements Service
{
	// SECTION: INSTANCE VARIABLES

	private Map<String, Serializer> serializers;
	private Map<String, Deserializer> deserializers;

	
	// SECTION: SERVICE

	@Override
	public Object deserialize(Request request)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object process(Request request, Object message)
	throws ServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response serialize(Request request, Object object)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
