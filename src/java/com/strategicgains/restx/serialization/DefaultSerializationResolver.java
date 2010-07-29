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

package com.strategicgains.restx.serialization;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.strategicgains.restx.Request;
import com.strategicgains.restx.Resolver;
import com.strategicgains.restx.exception.BadRequestException;

/**
 * @author toddf
 * @since Nov 20, 2009
 */
public class DefaultSerializationResolver
implements Resolver<Serializer>
{
	private Map<String, Serializer> serializers = new ConcurrentHashMap<String, Serializer>();

	@Override
	public Serializer resolve(Request request)
	{
		Serializer serializer = null;

		for (String acceptHeader : getAcceptHeaders(request))
		{
			serializer = serializers.get(acceptHeader);
			
			if (serializer != null)
			{
				break;
			}
		}
		
		if (serializer == null)
		{
			throw new BadRequestException("No serializer found for Accept Headers");
		}
		
		return serializer;
	}

	/**
     * @param request
     * @return
     */
    private List<String> getAcceptHeaders(Request request)
    {
	    // TODO Auto-generated method stub
	    return null;
    }
}
