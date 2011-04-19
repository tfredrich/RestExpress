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
package com.strategicgains.restexpress.domain;

/**
 * An immutable object representing an ID with an href property.
 * 
 * @author toddf
 * @since Jul 23, 2010
 */
public class XLink
{
	private String id;
	private String rel;
	private String href;

	public XLink(String id, String href)
	{
		this(id, null, href);
	}

	public XLink(String id, String rel, String href)
    {
	    super();
	    this.id = id;
	    this.href = href;
	    this.rel = rel;
    }

	public String id()
    {
    	return id;
    }

	public String href()
    {
    	return href;
    }

	public String rel()
	{
		return rel;
	}
}
