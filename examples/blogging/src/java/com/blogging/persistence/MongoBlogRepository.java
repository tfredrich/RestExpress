package com.blogging.persistence;

import java.util.List;

import com.blogging.domain.Blog;
import com.mongodb.ServerAddress;
import com.strategicgains.repoexpress.event.DefaultTimestampedIdentifiableRepositoryObserver;
import com.strategicgains.repoexpress.mongodb.MongodbRepository;
import com.strategicgains.repoexpress.mongodb.ObjectIdAdapter;

public class MongoBlogRepository
extends MongodbRepository<Blog>
implements BlogRepository
{
	private static final String DATABASE_NAME = "blogging";

	@SuppressWarnings("unchecked")
    public MongoBlogRepository(List<ServerAddress> bootstraps)
    {
	    super(bootstraps, DATABASE_NAME, Blog.class);
	    initializeObservers();
	    setIdentifierAdapter(new ObjectIdAdapter());
    }

    private void initializeObservers()
    {
    	addObserver(new DefaultTimestampedIdentifiableRepositoryObserver<Blog>());
    }
}
