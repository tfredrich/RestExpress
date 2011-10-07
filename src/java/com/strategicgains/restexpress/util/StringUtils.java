/*
    Copyright 2011, Strategic Gains, Inc.

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

import java.util.HashMap;
import java.util.Map;

/**
 * @author toddf
 * @since Oct 7, 2011
 */
public abstract class StringUtils
{
	/**
	 * Parses a delimited query-string into a Map. Also works for url-form-encoded request bodies.
	 * 
	 * @param queryString is a delimited query string.
	 * @return Map of name/value pairs, never null.
	 */
	public static Map<String, String> parseQueryString(String queryString)
	{
		final Map<String, String> queryStringMap = new HashMap<String, String>();
		iterateQueryString(queryString, new QueryStringCallback()
		{
			@Override
			public void assign(String key, String value)
			{
				queryStringMap.put(key, value);
			}
		});

		return queryStringMap;
	}
	
	public static void iterateQueryString(String queryString, QueryStringCallback callback)
	{
		if (queryString != null && !queryString.trim().isEmpty())
		{
			String[] params = queryString.split("&");
			
			for (String pair : params)
			{
				String[] keyValue = pair.split("=");
				String key = keyValue[0];
				
				if (keyValue.length == 1)
				{
					callback.assign(key, "");
				}
				else
				{
					callback.assign(key, keyValue[1]);
				}
			}
		}
	}

	private StringUtils()
	{
		// prevents instantiation.
	}
	
	public interface QueryStringCallback
	{
		public void assign(String key, String value);
	}
}
