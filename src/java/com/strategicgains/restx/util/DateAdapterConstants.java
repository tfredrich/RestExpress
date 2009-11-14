/*
 * Copyright 2009, Strategic Gains, Inc.
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

package com.strategicgains.restx.util;

import java.util.TimeZone;

/**
 * @author toddf
 * @since Nov 13, 2009
 */
public final class DateAdapterConstants
{
	public static final TimeZone COMMON_TIME_ZONE = TimeZone.getTimeZone("UTC");

	public static final String DATE_OUTPUT_FORMAT = "yyyy-MM-dd";
	public static final String[] DATE_INPUT_FORMATS =
	{
		DATE_OUTPUT_FORMAT,
		"yyyy/MM/dd",
		"MM/dd/yyyy",
		"yyyy-MM-dd'T'HH:mm:ssz"
	};
	public static final String TIMESTAMP_OUTPUT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSz";
	public static final String[] TIMESTAMP_INPUT_FORMATS =
	{
		TIMESTAMP_OUTPUT_FORMAT,
		"yyyy-MM-dd'T'HH:mm:ssz",
		"yyyy-MM-dd'T'HH:mmz",
		"yyyy-MM-dd"
	};

	private DateAdapterConstants()
    {
		// prevents instantiation.
    }
}
