package com.blogging.domain;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.strategicgains.syntaxe.annotation.Validate;

@Entity("comments")
public class Comment
extends BaseDomainObject
{
	@Indexed
	@Validate(name="Blog Entry ID", required=true)
	private ObjectId blogEntryId;
	
	@Validate(name="Author", required=true)
	private String author;
	
	@Validate(name="Comment Content", required=true)
	private String content;

	public ObjectId getBlogEntryId()
    {
    	return blogEntryId;
    }

	public void setBlogEntryId(ObjectId blogEntryId)
    {
    	this.blogEntryId = blogEntryId;
    }

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}
}
