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
package com.strategicgains.restx.url;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains the results of a UrlPattern.match() call, reflecting the match outcome
 * and containing any parameter values, if applicable.
 * 
 * <p/>UrlMatch is immutable.
 * 
 * @author toddf
 * @since Apr 29, 2010
 */
public class UrlMatch
{
	/**
	 * True when the URL matches the pattern.
	 */
	private boolean matches;
	
	/**
	 * Parameter values parsed from the URL during the match.
	 */
	private Map<String, String>parameters = new HashMap<String, String>();

	
	// SECTION: CONSTRUCTOR

	public UrlMatch(boolean matches, Map<String, String> parameters)
	{
		super();
		this.matches = matches;
		
		if (parameters != null)
		{
			this.parameters.putAll(parameters);
		}
	}

	
	// SECTION: ACCESSORS

	/**
	 * Retrieves a parameter value parsed from the URL during the match.
	 * 
	 * @param name the name of a parameter for which to retrieve the value.
	 * @return the parameter value from the URL, or null if not present.
	 */
	public String get(String name)
	{
		return parameters.get(name);
	}

	/**
	 * Answer whether the URL matched the pattern.
	 * 
	 * @return true with the URL matches the pattern.
	 */
	public boolean matches()
	{
		return matches;
	}
}
