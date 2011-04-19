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
import java.util.List;
import java.util.Map;

import com.strategicgains.restexpress.domain.DefaultXLinkFactory;
import com.strategicgains.restexpress.domain.XLink;
import com.strategicgains.restexpress.domain.XLinkFactory;

/**
 * Non-instantiable class with foreign methods to create and manipulate XLink instances.
 * 
 * @author toddf
 * @since July 29, 2010
 */
public abstract class XLinkUtils
{
	private static final XLinkFactory DEFAULT_XLINK_FACTORY = new DefaultXLinkFactory();

	private XLinkUtils()
	{
		// This constructor prevents instantiation.
	}

	/**
	 * Simply creates a List of XLink instances where the resulting path is the passed-in urlPath suffixed with
	 * one of the ids.
	 * 
	 * @param ids a Collection of identifiers to create XLink instances for.
	 * @param paramName the URL parameter that these identifiers represent.
	 * @param urlPath the URL that will retrieve individual ids.  There should be a parameter in it that matches the
	 *                paramName, above.
	 * @param nameValuePairs is a sequence of name/value pairs, where the name matches parameters in urlPath and the
	 *                       value is what gets substituted.
	 * @return a List of XLink instances.
	 */
	public static List<XLink> asXLinks(Collection<String> ids, String paramName, String urlPath, String... nameValuePairs)
	{
		return asXLinks(ids, paramName, urlPath, DEFAULT_XLINK_FACTORY, nameValuePairs);
	}
	

	/**
	 * Creates a List of XLink instances where the resulting path is the passed-in urlPath suffixed with
	 * one of the ids.  Calls the xlinkCallback after creation of each XLink instance to allow clients of this
	 * method to augment the values within the XLink itself.
	 * 
	 * @param ids a Collection of identifiers to create XLink instances for.
	 * @param paramName the URL parameter that these identifiers represent.
	 * @param urlPath the URL that will retrieve individual ids.  There should be a parameter in it that matches the
	 *                paramName, above.
	 * @param xlinkFactory a caller-provided class that creates alternate XLink forms.
	 * @param nameValuePairs is a sequence of name/value pairs, where the name matches parameters in urlPath and the
	 *                       value is what gets substituted.
	 * @return a List of XLink instances.
	 */
	public static List<XLink> asXLinks(Collection<String> ids, String paramName, String urlPath, XLinkFactory xlinkFactory, String... nameValuePairs)
	{
		MapStringFormat formatter = new MapStringFormat();
		Map<String, String> parameters = MapStringFormat.toMap(nameValuePairs);
		List<XLink> results = new ArrayList<XLink>(ids.size());

		for (String id : ids)
		{
			parameters.put(paramName, id);
			results.add(xlinkFactory.create(id, formatter.format(urlPath, parameters)));
		}

		return results;
	}
}
