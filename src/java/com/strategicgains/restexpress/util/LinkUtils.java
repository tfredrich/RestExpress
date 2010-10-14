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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.strategicgains.restexpress.domain.Link;
import com.strategicgains.restexpress.exception.ServiceException;

/**
 * Non-instantiable class with foreign methods to create and manipulate Link instances.
 * 
 * @author toddf
 * @since July 29, 2010
 */
public abstract class LinkUtils
{
	private LinkUtils()
	{
		// This constructor prevents instantiation.
	}
	
	/**
	 * Simply creates a List of Link instances where the resulting path is the passed-in urlPath suffixed with
	 * one of the ids.
	 * 
	 * @param ids
	 * @param urlPath
	 * @return
	 */
	public static List<Link> asLinks(Collection<String> ids, String paramName, String urlPath, String... nameValuePairs)
	{
		MapStringFormat formatter = new MapStringFormat();
		Map<String, String> parameters = toMap(nameValuePairs);
		List<Link> results = new ArrayList<Link>();
		
		for (String id : ids)
		{
			parameters.put(paramName, id);
			results.add(new Link(id, formatter.format(urlPath, parameters)));
		}
		
		return results;
	}

	private static Map<String, String> toMap(String... nameValuePairs)
	{
		if ((nameValuePairs.length % 2) != 0)
		{
			throw new ServiceException("Name/value pairs unbalanced: " + nameValuePairs.toString());
		}

		Map<String, String> result = new HashMap<String, String>();

		for (int i = 0; i < nameValuePairs.length; i += 2)
		{
			result.put(nameValuePairs[i], nameValuePairs[i + 1]);
		}

		return result;
	}
}
