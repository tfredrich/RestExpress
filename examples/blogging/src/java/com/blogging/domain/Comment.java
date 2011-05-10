package com.blogging.domain;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.strategicgains.syntaxe.annotation.StringValidation;

@Entity("comments")
public class Comment
extends BaseDomainObject
{
	@Indexed
	@StringValidation(name="Blog Entry ID", required=true)
	private ObjectId blogEntryId;
	
	@StringValidation(name="Author", required=true)
	private String author;
	
	@StringValidation(name="Comment Content", required=true)
	private String content;

	public String getBlogEntryId()
    {
    	return (blogEntryId == null ? null : blogEntryId.toString());
    }

	public void setBlogEntryId(String blogEntryId)
    {
    	this.blogEntryId = (blogEntryId == null ? null : new ObjectId(blogEntryId));
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
