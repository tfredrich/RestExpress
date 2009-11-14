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

import static com.strategicgains.restx.util.DateFormatConstants.DATE_INPUT_FORMATS;
import static com.strategicgains.restx.util.DateFormatConstants.DATE_OUTPUT_FORMAT;

import java.text.ParseException;
import java.util.Date;


/**
 * @author toddf
 * @since Nov 13, 2009
 */
public class DateFormatter
implements Formatter<Date>
{
	// SECTION: INSTANCE VARIABLES

	private DateFormatProcessor processor;

	
	// SECTION: CONSTRUCTOR

	public DateFormatter()
	{
		this(DATE_INPUT_FORMATS, DATE_OUTPUT_FORMAT);
	}
	
	protected DateFormatter(String[] inputFormats, String outputFormat)
	{
		this.processor = new DateFormatProcessor(inputFormats, outputFormat);
	}

	
	// SECTION: FORMATTING
	
	@Override
	public Date fromString(String dateString)
	throws ParseException
	{
		return processor.fromString(dateString);
	}
	
	@Override
	public String asString(Date date)
	{
		return processor.asString(date);
	}
}
