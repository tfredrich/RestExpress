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

import java.util.ArrayList;
import java.util.List;

import com.strategicgains.restexpress.Request;

/**
 * @author toddf
 * @since Apr 12, 2011
 */
public class QueryOrder
{
	private static final String SORT_HEADER_NAME = "sort";
	private static final String SORT_SEPARATOR = "\\|";

	private List<OrderComponent> sorts = null;
	
	public QueryOrder()
	{
		super();
	}
	
	public QueryOrder(String[] strings)
	{
		this();
		
		if (strings == null || strings.length == 0) return;

		sorts = new ArrayList<OrderComponent>();

		for (String sortString : strings)
		{
			boolean isDescending = sortString.startsWith("-");
			String fieldName = sortString.replaceAll("^[+-]{1}", "");
			sorts.add(new OrderComponent(fieldName, isDescending));
		}
	}

	public boolean isSorted()
	{
		return (sorts != null && !sorts.isEmpty());
	}

	public void iterate(OrderCallback callback)
	{
		if (callback == null || !isSorted()) return;

		for (OrderComponent component : sorts)
		{
			callback.orderBy(component);
		}
	}
	
	public static QueryOrder parseFrom(Request request)
	{
		String sortString = request.getUrlDecodedHeader(SORT_HEADER_NAME);

		if (sortString == null || sortString.trim().isEmpty())
		{
			return new QueryOrder();
		}
		
		String[] strings = sortString.split(SORT_SEPARATOR);
		return new QueryOrder(strings);
	}
}
