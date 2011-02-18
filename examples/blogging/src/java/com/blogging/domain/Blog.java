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
package com.blogging.domain;

import com.google.code.morphia.annotations.Entity;
import com.strategicgains.syntaxe.annotation.Validate;

/**
 * @author toddf
 * @since Feb 15, 2011
 */
@Entity("blogs")
public class Blog
extends BaseDomainObject
{
	@Validate(name = "Blog Title", required = true)
	private String title;
	private String description;
	
	@Validate(name = "Blog Author", required=true)
	private String author;

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}
}
