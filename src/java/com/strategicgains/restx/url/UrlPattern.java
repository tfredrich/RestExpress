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
 * @author toddf
 * @since Apr 28, 2010
 */
public class UrlPattern
{
	private static final String URL_PARAM_REGEX = "\\{(.*?)\\}";
	private static final String URL_MATCH_REGEX = "\\(.+?\\)";
	private static final Pattern URL_PARAM_PATTERN = Pattern.compile(URL_PARAM_REGEX);

	private static final String URL_FORMAT_REGEX = "(?:\\.\\{(.+)\\})$";
	private static final String URL_FORMAT_MATCH_REGEX = "\\\\.\\(.+?\\)\\$";

	private String urlPattern;
	private Pattern compiledUrl;
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
		compiledUrl = Pattern.compile(parsedPattern);
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
