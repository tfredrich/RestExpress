package com.blogging.controller;

import com.blogging.Constants;
import com.blogging.domain.Blog;
import com.blogging.persistence.BlogRepository;
import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;

/**
 * @author toddf
 * @since Aug 31, 2010
 */
public class BlogController
{
	private BlogRepository repo;
	
	public BlogController(BlogRepository repository)
	{
		super();
		this.repo = repository;
	}

	public Blog create(Request request, Response response)
	{
		Blog blog = request.getBodyAs(Blog.class, "Blog data not provided.");
		blog.validate();
		Blog result = repo.create(blog);
		response.setResponseCreated();
		return result;
	}

	public Blog read(Request request, Response response)
	{
		String id = request.getUrlDecodedHeader(Constants.BLOG_ID_HEADER, "Blog ID not provided.");
		return repo.read(id);
	}

	public void update(Request request, Response response)
	{
		Blog blog = request.getBodyAs(Blog.class, "Blog data not provided.");
		blog.validate();
		repo.update(blog);
	}

	public void delete(Request request, Response response)
	{
		String id = request.getUrlDecodedHeader(Constants.BLOG_ID_HEADER, "Blog ID not provided.");
		repo.delete(id);
	}
}
