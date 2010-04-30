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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UrlPattern leverages Regex Pattern to represent a parameterized URL. Parameters within the URL are
 * denoted by curly braces '{}' with the parameter name contained within (e.g. '{userid}').
 * 
 * <p/>Parameter names must be formed of word characters (e.g. A-Z, a-z, 0-9, '_').
 * <p/>An optional format parameter following a dot ('.') may be added to the end.
 * @author toddf
 * @since Apr 28, 2010
 */
public class UrlPattern
{
	// SECTION: CONSTANTS

	// Finds parameters in the URL pattern string.
	private static final String URL_PARAM_REGEX = "\\{(\\w*?)\\}";
	
	// Replaces parameter names in the URL pattern string before compilation to match URLs. 
	private static final String URL_MATCH_REGEX = "\\(\\\\w+?\\)";
	
	// Pattern to match URL pattern parameter names.
	private static final Pattern URL_PARAM_PATTERN = Pattern.compile(URL_PARAM_REGEX);

	// Finds the format portion of the URL pattern string.
	private static final String URL_FORMAT_REGEX = "(?:\\.\\{(\\w+)\\})$";
	
	// Replaces the format parameter name in the URL pattern string before compilation to match URLs.
	private static final String URL_FORMAT_MATCH_REGEX = "(?:\\\\.\\(\\\\w+?\\))?";
	
	// Finds the query string in a URL.
	private static final String URL_QUERY_STRING_REGEX = "(?:\\?.+?)?$";

	/**
	 * The URL pattern describing the URL layout and any parameters.
	 */
	private String urlPattern;
	
	/**
	 * A compiled regex created from the urlPattern, above.
	 */
	private Pattern compiledUrl;
	
	/**
	 * An ordered list of parameter names found in the urlPattern, above.
	 */
	private List<String> parameterNames = new ArrayList<String>();

	/**
	 * @param pattern
	 */
	public UrlPattern(String pattern)
	{
		super();
		setUrlPattern(pattern);
		compile();
	}

	/**
     * @return the pattern
     */
    private String getUrlPattern()
    {
    	return urlPattern;
    }

	/**
     * @param pattern the pattern to set
     */
    private void setUrlPattern(String pattern)
    {
    	this.urlPattern = pattern;
    }

	public UrlMatch matches(String url)
	{
		Matcher matcher = compiledUrl.matcher(url);

		if (matcher.matches())
		{
			return extractParameters(matcher);
		}

		return null;
	}
	
	public void compile()
	{
		acquireParameterNames();
		String parsedPattern = getUrlPattern().replaceFirst(URL_FORMAT_REGEX, URL_FORMAT_MATCH_REGEX);
		parsedPattern = parsedPattern.replaceAll(URL_PARAM_REGEX, URL_MATCH_REGEX);
		compiledUrl = Pattern.compile(parsedPattern + URL_QUERY_STRING_REGEX);
	}

	private void acquireParameterNames()
    {
	    Matcher m = URL_PARAM_PATTERN.matcher(getUrlPattern());

		while (m.find())
		{
			parameterNames.add(m.group(1));
		}
    }

	private UrlMatch extractParameters(Matcher matcher)
    {
	    Map<String, String> values = new HashMap<String, String>();
	    
	    for (int i = 0; i < matcher.groupCount(); i++)
	    {
	    	values.put(parameterNames.get(i), matcher.group(i + 1));
	    }

	    return new UrlMatch(values);
    }
}
