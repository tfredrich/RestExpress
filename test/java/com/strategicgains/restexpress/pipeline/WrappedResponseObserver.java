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
package com.strategicgains.restexpress.pipeline;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;

/**
 * @author toddf
 * @since Feb 10, 2011
 */
public class WrappedResponseObserver
extends MessageObserver
{
	private int receivedCount = 0;
	private int exceptionCount = 0;
	private int successCount = 0;
	private int completeCount = 0;

	@Override
    protected void onReceived(Request request, Response response)
    {
		++receivedCount;
    }

	@Override
    protected void onException(Throwable exception, Request request, Response response)
    {
		++exceptionCount;
    }

	@Override
    protected void onSuccess(Request request, Response response)
    {
		++successCount;
    }

	@Override
    protected void onComplete(Request request, Response response)
    {
		++completeCount;
    }

	public int getReceivedCount()
    {
    	return receivedCount;
    }

	public int getExceptionCount()
    {
    	return exceptionCount;
    }

	public int getSuccessCount()
    {
    	return successCount;
    }

	public int getCompleteCount()
    {
    	return completeCount;
    }
}
