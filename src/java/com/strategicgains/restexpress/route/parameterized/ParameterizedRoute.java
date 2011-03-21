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
package com.strategicgains.restexpress.route.parameterized;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.strategicgains.restexpress.route.Route;
import com.strategicgains.restexpress.url.UrlPattern;


/**
 * @author toddf
 * @since Jan 7, 2011
 */
public class ParameterizedRoute
extends Route
{
	/**
     * @param urlMatcher
     * @param controller
     * @param action
     * @param method
     * @param shouldSerializeResponse
     * @param name
     */
    public ParameterizedRoute(UrlPattern urlMatcher, Object controller, Method action, HttpMethod method, boolean shouldSerializeResponse,
    	boolean shouldUseWrappedResponse, String name, Set<String> flags, Map<String, String> parameters)
    {
	    super(urlMatcher, controller, action, method, shouldSerializeResponse, shouldUseWrappedResponse, name, flags, parameters);
    }

    /**
     * @param urlPattern
     * @param controller
     * @param action
     * @param method
     * @param shouldSerializeResponse
     * @param name
     */
    public ParameterizedRoute(String urlPattern, Object controller, Method action, HttpMethod method, boolean shouldSerializeResponse,
    	boolean shouldUseWrappedResponse, String name, Set<String> flags, Map<String, String> parameters)
    {
	    this(new UrlPattern(urlPattern), controller, action, method, shouldSerializeResponse, shouldUseWrappedResponse, name, flags, parameters);
    }
}
