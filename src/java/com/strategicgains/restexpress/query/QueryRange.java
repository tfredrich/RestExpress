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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.exception.BadRequestException;

/**
 * Supports the concept of 'pagination' via request 'Range' parameters.
 * <p/>
 * Paging is accomplished using the Range and Content-Range HTTP headers.  The client can request a range of
 * results by including the "Range" header with the request.  For example, to get the first 25 results:
 * <p/>
 * GET /many_things.json<br/>
 * HTTP/1.1<br/>
 * Host: example.com<br/>
 * Range: items=0-24<br/>
 * 
 * The server will respond with a "Content-Range" header that includes the start and end of the range, as well
 * as a total count of all results.  For example, the response for the first 25 of 67 total results:
 * <p/>
 * HTTP/1.1 200 OK<br/>
 * Content-Type: application/json<br/>
 * Content-Range: items 0-24/67<br/>
 * 
 * @author toddf
 * @since Apr 11, 2011
 */
public class QueryRange
{
	// SECTION: CONSTANTS

	private static final String RANGE_HEADER_NAME = "Range";
	private static final String ITEMS_HEADER_REGEX = "items=(\\d+)-(\\d+)";
	private static final Pattern ITEMS_HEADER_PATTERN = Pattern.compile(ITEMS_HEADER_REGEX);


	// SECTION: INSTANCE VARIABLES

	private Long start = null;
	private Long stop = null;

	
	// SECTION: CONSTRUCTORS

	public QueryRange()
	{
		super();
	}

	public QueryRange(long start, long stop)
	{
		super();
		setStart(start);
		setStop(stop);
	}

	public QueryRange(long start, int offset)
	{
		super();

		if (offset == 0)
		{
			setStart(0);
			setStop(0);
		}
		else
		{
			setStart(start);
			setOffset(offset);
		}
	}

	
	// SECTION: ACCESSORS / MUTATORS

	public int getOffset()
	{
		return (int) (getStop() - getStart());
	}

	public boolean hasStart()
	{
		return (start != null);
	}

	public long getStart()
	{
		return (start == null ? 0 : start.intValue());
	}

	public void setStart(long value)
	{
		this.start = Long.valueOf(value);
	}

	public long getStop()
	{
		return (stop == null ? 0 : stop.intValue());
	}

	public boolean hasStop()
	{
		return (stop != null);
	}

	public void setStop(long value)
	{
		this.stop = Long.valueOf(value);
	}
	
	public void setOffset(int value)
	{
		setStop(getStart() + value - 1);
	}
	
	/**
	 * Return true if the range has start and stop values.
	 * 
	 * @return true if the range is initialized (has start and stop values)
	 */
	public boolean isInitialized()
	{
		return hasStart() && hasStop();
	}

	/**
	 * Validates the range.
	 * 
	 * @return true if the range is valid
	 */
	public boolean isValid()
	{
		return (getStart() <= getStop());
	}
	
	
	// SECTION: FACTORY


	/**
	 * Create a QueryRange instance from the current RestExpress request, providing a
	 * default maximum offset if the request contains no range criteria.
	 * 
	 * @param request the current request
	 * @param maxResults the default maximum offset, used if the request contains no range criteria
	 * @return a QueryRange instance
	 */
	public static QueryRange parseFrom(Request request, int maxResults)
	{
		QueryRange range = new QueryRange(0, maxResults);
		parseInto(request, range);
		return range;
	}

	/**
	 * Create a QueryRange instance from the current RestExpress request.
	 * 
	 * @param request the current request
	 * @return a QueryRange instance
	 */
	public static QueryRange parseFrom(Request request)
	{
		QueryRange range = new QueryRange();
		parseInto(request, range);
		return range;
	}
	
	private static void parseInto(Request request, QueryRange range)
	{
		String rangeHeader = request.getUrlDecodedHeader(RANGE_HEADER_NAME);

		if (rangeHeader != null && !rangeHeader.trim().isEmpty())
		{
			Matcher matcher = ITEMS_HEADER_PATTERN.matcher(rangeHeader);

			if(!matcher.matches())
			{
				throw new BadRequestException("Unparseable 'Range' header.  Expecting items=[start]-[end] was: " + rangeHeader);
			}

			range.setStart(Long.parseLong(matcher.group(1)));
			range.setStop(Long.parseLong(matcher.group(2)));
			
			if (!range.isValid())
			{
				throw new BadRequestException("Invalid 'Range' header.  Expecting items=[start]-[end]  was: " + rangeHeader);
			}
		}
	}
	
	@Override
	public String toString()
	{
		return "items " + getStart() + "-" + getOffset();
	}
}
