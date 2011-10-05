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
package com.blogging.postprocessor;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.ETAG;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.LAST_MODIFIED;

import com.strategicgains.repoexpress.domain.Timestamped;
import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.pipeline.Postprocessor;
import com.strategicgains.util.date.DateAdapter;
import com.strategicgains.util.date.HttpHeaderTimestampAdapter;

/**
 * If the response body is non-null, adds an ETag header.  In addition, if
 * the response body is a Timestamped instance, the a Last-Modified header
 * is also added.
 * <p/>
 * ETag is computed from the object hash code.  This will cause issues if
 * object caching is strictly on the ETag as different representations
 * (e.g. XML or JSON) will have the same ETag.
 * 
 * @author toddf
 * @since Oct 5, 2011
 */
public class EtagHeaderPostprocessor
implements Postprocessor
{
	DateAdapter fmt = new HttpHeaderTimestampAdapter();

	@Override
	public void process(Request request, Response response)
	{
		if (!request.isMethodGet()) return;
		if (!response.hasBody()) return;

		Object body = response.getBody();

		if (!response.hasHeader(ETAG))
		{
			response.addHeader(ETAG, String.valueOf(body.hashCode()));
		}

		if (!response.hasHeader(LAST_MODIFIED) && body.getClass().isAssignableFrom(Timestamped.class))
		{
			response.addHeader(LAST_MODIFIED, fmt.format(((Timestamped) body).getUpdatedAt()));
		}
	}
}
