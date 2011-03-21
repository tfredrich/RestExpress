package com.blogging.persistence;

import java.util.List;

import org.bson.types.ObjectId;

import com.blogging.domain.Comment;
import com.google.code.morphia.query.Query;
import com.mongodb.ServerAddress;
import com.strategicgains.repoexpress.event.DefaultTimestampedIdentifiableRepositoryObserver;
import com.strategicgains.repoexpress.mongodb.MongodbRepository;
import com.strategicgains.repoexpress.mongodb.ObjectIdAdapter;

public class MongoCommentRepository
extends MongodbRepository<Comment>
implements CommentRepository
{
	private static final String DATABASE_NAME = "blogging";

	@SuppressWarnings("unchecked")
	public MongoCommentRepository(List<ServerAddress> bootstraps)
	{
		super(bootstraps, DATABASE_NAME, Comment.class);
		initializeObservers();
		setIdentifierAdapter(new ObjectIdAdapter());
	}

	private void initializeObservers()
	{
		addObserver(new DefaultTimestampedIdentifiableRepositoryObserver<Comment>());
	}

	@Override
	public List<Comment> readAll(String blogEntryId)
	{
		Query<Comment> query = getDataStore().find(Comment.class, "blogEntryId", new ObjectId(blogEntryId)).order("createdAt");
		return query.asList();
	}
}
