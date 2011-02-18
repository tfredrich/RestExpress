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
package com.blogging.serialization;

import com.blogging.domain.Blog;
import com.blogging.domain.BlogEntry;
import com.blogging.domain.Comment;
import com.strategicgains.restexpress.serialization.xml.DefaultXmlProcessor;

/**
 * @author toddf
 * @since Feb 16, 2011
 */
public class BlogXmlProcessor
extends DefaultXmlProcessor
{
	public BlogXmlProcessor()
    {
	    super();
		alias("blog", Blog.class);
		alias("entry", BlogEntry.class);
		alias("comment", Comment.class);
		registerConverter(new XstreamObjectIdConverter());
    }
}
