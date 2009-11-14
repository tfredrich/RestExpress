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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Responsible for using converting java.util.Data instances to and from strings.
 * 
 * <p>The constructor accepts a list of input formats (see SimpleDateFormat) and an output format.
 * the input formats are those date formats, in priority order, that the input accepts as valid.
 * The output format is the date format always used for output.  It is good form to have the first
 * input format match the output format.
 * 
 * @author Todd Fredrich
 * @since Nov 13, 2009
 */
public class DateFormatProcessor
{
	private DateFormat[] inputFormats;
	private DateFormat outputFormat;
	
	public DateFormatProcessor(List<String> inputFormats, String outputFormat)
	{
		this(inputFormats.toArray(new String[0]), outputFormat);
	}

	public DateFormatProcessor(String[] inputFormats, String outputFormat)
	{
		format.setTimezone(COMMON_TIME_ZONE);
		this.inputFormats = Arrays.copyOf(inputFormats, inputFormats.length);
		this.outputFormat = outputFormat;
	}
	
	/**
	 * Attempts to parse the given string into a java.util.Date using the provided
	 * input formats.
	 * 
	 * @param dateString a date string in one of the acceptable formats.
	 * @throws ParseException if the date is not in one of the input formats.
	 */
	public Date fromString(String dateString)
	throws ParseException
	{
		Date result = null;
		ParseException lastException = null;
		
		for (String format : inputFormats)
		{
			try
			{
				result = new SimpleDateFormat(format).parse(dateString);
				lastException = null;
				break;
			}
			catch (ParseException e)
			{
				// Keep the first exception that occurred.
				if (lastException == null)
				{
					lastException = e;
				}
				// And just try the next input format.
			}
		}

		if (lastException != null)
		{
			throw lastException;
		}

		return result;
	}
	
	/**
	 * Formats the given java.util.Date into a string using the output format provided in the
	 * constructor.
	 * 
	 * @param date a java.util.Date
	 */
	public String asString(Date date)
	{
		return new SimpleDateFormat(outputFormat).format(date);
	}
}
