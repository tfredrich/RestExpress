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
package com.strategicgains.restexpress;

import java.nio.charset.Charset;

/**
 * @author toddf
 * @since Jan 19, 2011
 */
public abstract class ContentType
{
	public static final String ENCODING = "UTF-8";
	public static final Charset CHARSET = Charset.forName(ENCODING);

	public static final String HTML = "text/html; charset=" + ENCODING;
	public static final String JAVASCRIPT = "application/javascript; charset=" + ENCODING;
	public static final String JSON = "application/json; charset=" + ENCODING;
	public static final String TEXT_PLAIN = "text/plain; charset=" + ENCODING;
	public static final String XML = "application/xml; charset=" + ENCODING;
	
	private ContentType()
	{
		// prevents instantiation.
	}
}
