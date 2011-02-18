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
package com.blogging.persistence;

import java.util.List;

import com.blogging.domain.BaseDomainObject;
import com.blogging.domain.Blog;
import com.mongodb.ServerAddress;
import com.strategicgains.repoexpress.event.DefaultTimestampedIdentifiableRepositoryObserver;
import com.strategicgains.repoexpress.mongodb.MongodbRepository;
import com.strategicgains.repoexpress.mongodb.ObjectIdAdapter;

/**
 * @author toddf
 * @since Feb 17, 2011
 */
public abstract class AbstractMongoDbRepository<T extends BaseDomainObject>
extends MongodbRepository<T>
{
	private static final String DATABASE_NAME = "blogging";

	@SuppressWarnings("unchecked")
    public AbstractMongoDbRepository(List<ServerAddress> bootstraps, Class<T> type)
    {
	    super(bootstraps, DATABASE_NAME, type);
	    initializeObservers();
	    setIdentifierAdapter(new ObjectIdAdapter());
    }

    private void initializeObservers()
    {
    	addObserver(new DefaultTimestampedIdentifiableRepositoryObserver<Blog>());
    }

}
