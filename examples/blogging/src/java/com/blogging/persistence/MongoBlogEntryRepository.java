package com.blogging.persistence;

import java.util.List;

import org.bson.types.ObjectId;

import com.blogging.domain.BlogEntry;
import com.google.code.morphia.query.Query;
import com.mongodb.ServerAddress;
import com.strategicgains.repoexpress.event.DefaultTimestampedIdentifiableRepositoryObserver;
import com.strategicgains.repoexpress.mongodb.MongodbRepository;
import com.strategicgains.repoexpress.mongodb.ObjectIdAdapter;

public class MongoBlogEntryRepository
extends MongodbRepository<BlogEntry>
implements BlogEntryRepository
{
	private static final String DATABASE_NAME = "blogging";

	@SuppressWarnings("unchecked")
	public MongoBlogEntryRepository(List<ServerAddress> bootstraps)
	{
		super(bootstraps, DATABASE_NAME, BlogEntry.class);
		initializeObservers();
		setIdentifierAdapter(new ObjectIdAdapter());
	}

	private void initializeObservers()
	{
		addObserver(new DefaultTimestampedIdentifiableRepositoryObserver<BlogEntry>());
	}

	@Override
	public List<BlogEntry> readAll(String blogId)
	{
		Query<BlogEntry> query = getDataStore().find(BlogEntry.class, "blogId", new ObjectId(blogId)).order("-createdAt");
		return query.asList();
	}
}
