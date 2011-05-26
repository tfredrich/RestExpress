/*
 * Copyright 2011, Strategic Gains, Inc.
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
package com.strategicgains.restexpress.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.strategicgains.restexpress.Request;

/**
 * Supports the concept of filtering a result based on the 'filter' query parameter.
 * a list of field name/value pairs separated by a vertical bar ('|') and the field name
 * separated from the value with two colons ('::').
 * <p/>
 * To filter on name: ?sort=name::todd
 * <p/>
 * To filter on name and description: ?sort=name::todd|description::amazing
 * 
 * @author toddf
 * @since Apr 12, 2011
 */
public class QueryFilter
{
	private static final String FILTER_HEADER_NAME = "filter";
	private static final String FILTER_SEPARATOR = "\\|";
	private static final String NAME_VALUE_SEPARATOR = "::";

	private Map<String, String> filters = null;
	
	public QueryFilter()
	{
		super();
	}
	
	public QueryFilter(Map<String, String> filters)
	{
		this();
		this.filters = new HashMap<String, String>(filters);
	}

	/**
	 * Returns true if this QueryFilter instance would affect the query (has effective filters).
	 * 
	 * @return true if filters exist within this QueryFilter instance
	 */
	public boolean hasFilters()
	{
		return (filters != null && !filters.isEmpty());
	}
	
	/**
	 * Iterate the filter criteria within this QueryFilter, invoking the FilterCallback
	 * to presumably construct a query.
	 * 
	 * @param callback a FilterCallback instance
	 */
	public void iterate(FilterCallback callback)
	{
		if (callback == null || !hasFilters()) return;

		for (Entry<String, String> entry : filters.entrySet())
		{
			callback.filterOn(entry.getKey(), entry.getValue());
		}
	}
	
	
	// SECTION: FACTORY

	/**
	 * Create an instance of QueryFilter from the RestExpress request.
	 * 
	 * @param request the current request
	 */
	public static QueryFilter parseFrom(Request request)
	{
		String filterString = request.getUrlDecodedHeader(FILTER_HEADER_NAME);
		
		if (filterString == null || filterString.trim().isEmpty())
		{
			return new QueryFilter();
		}
		
		String[] nameValues = filterString.split(FILTER_SEPARATOR);

		if (nameValues == null || nameValues.length == 0)
		{
			return new QueryFilter();
		}

		String[] nameValuePair;
		Map<String, String> filters = new HashMap<String, String>();
		
		for (String nameValue : nameValues)
		{
			nameValuePair = nameValue.split(NAME_VALUE_SEPARATOR);
			
			if (nameValuePair.length == 1)
			{
				filters.put(nameValuePair[0], "");
			}
			else
			{
				filters.put(nameValuePair[0], nameValuePair[1]);
			}
		}

		return new QueryFilter(filters);
	}
}
