package com.blogging.controller;

import java.util.List;

import org.bson.types.ObjectId;

import com.blogging.Constants;
import com.blogging.domain.Comment;
import com.blogging.persistence.CommentRepository;
import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;

/**
 * @author toddf
 * @since Aug 31, 2010
 */
public class CommentController
{
	private CommentRepository repo;
	
	public CommentController(CommentRepository repository)
	{
		super();
		this.repo = repository;
	}

	public Comment create(Request request, Response response)
	{
		Comment comment = request.getBodyAs(Comment.class, "Comment data not provided.");
		String entryId = request.getUrlDecodedHeader(Constants.ENTRY_ID_HEADER, "Blog Entry ID not provided.");
		comment.setBlogEntryId(new ObjectId(entryId));
		comment.validate();
		Comment result = repo.create(comment);
		response.setResponseCreated();
		return result;
	}

	public Comment read(Request request, Response response)
	{
		String id = request.getUrlDecodedHeader(Constants.COMMENT_ID_HEADER, "Comment ID not provided.");
		return repo.read(id);
	}

	public Comment update(Request request, Response response)
	{
		Comment entry = request.getBodyAs(Comment.class, "Comment data not provided");
		entry.validate();
		return repo.update(entry);
	}

	public void delete(Request request, Response response)
	{
		String id = request.getUrlDecodedHeader(Constants.COMMENT_ID_HEADER, "Comment ID not provided.");
		repo.delete(id);
	}

	public List<Comment> list(Request request, Response response)
	{
		String entryId = request.getUrlDecodedHeader(Constants.ENTRY_ID_HEADER, "Blog Entry ID not provided.");
		return repo.readAll(entryId);
	}
}
