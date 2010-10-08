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
package com.strategicgains.restexpress.domain.validation;

import java.util.List;

/**
 * @author toddf
 * @since Oct 7, 2010
 */
public class Validations
{
	private Validations()
	{
		// Prevent instantiation.
	}
	
	public static void require(String name, String value, List<String> errors)
	{
    	if (value == null || value.trim().isEmpty())
    	{
    		errors.add(name + " is required");
    	}
	}
	
	public static void maxLength(String name, String value, int max, List<String> errors)
	{
		if (value == null) return;
		
		if (value.length() > max)
		{
			errors.add(name + " is limited to " + max + " characters.");
		}
	}

	public static void lessThan(String name, int actual, int max, List<String> errors)
	{
		if (actual >= max)
		{
			errors.add(name + " must be less-than " + max);
		}
	}

	public static void greaterThan(String name, int actual, int min, List<String> errors)
	{
		if (actual <= min)
		{
			errors.add(name + " must be greater-than " + min);
		}
	}
}
