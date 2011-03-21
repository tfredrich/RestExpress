package com.blogging.controller;

import java.util.List;

import com.blogging.Constants;
import com.blogging.domain.BlogEntry;
import com.blogging.persistence.BlogEntryRepository;
import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;

/**
 * @author toddf
 * @since Aug 31, 2010
 */
public class BlogEntryController
{
	private BlogEntryRepository repo;
	
	public BlogEntryController(BlogEntryRepository repository)
	{
		super();
		this.repo = repository;
	}

	public BlogEntry create(Request request, Response response)
	{
		BlogEntry entry = request.getBodyAs(BlogEntry.class, "No blog entry data provided.");
		entry.validate();
		BlogEntry result = repo.create(entry);
		response.setResponseCreated();
		return result;
	}

	public BlogEntry read(Request request, Response response)
	{
		String id = request.getHeader(Constants.ENTRY_ID_HEADER, "Blog entry ID not provided.");
		return repo.read(id);
	}

	public void update(Request request, Response response)
	{
		BlogEntry entry = request.getBodyAs(BlogEntry.class);
		entry.validate();
		repo.update(entry);
	}

	public void delete(Request request, Response response)
	{
		String id = request.getHeader(Constants.ENTRY_ID_HEADER, "Blog entry ID not provided.");
		repo.delete(id);
	}

	public List<BlogEntry> list(Request request, Response response)
	{
		String blogId = request.getHeader(Constants.BLOG_ID_HEADER, "Blog ID not provided.");
		return repo.readAll(blogId);
	}
}
