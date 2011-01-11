/*
    Copyright 2010, Strategic Gains, Inc.

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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.pipeline.MessageObserver;

/**
 * Provides simple System.out.println() details about basic timing.
 * 
 * @author toddf
 * @since Dec 16, 2010
 */
public class SimpleMessageObserver
extends MessageObserver
{
	// SECTION: INSTANCE VARIABLES

	private Map<String, Timer> timers = new ConcurrentHashMap<String, Timer>();

	
	// SECTION: MESSAGE OBSERVER

	@Override
    protected void onReceived(Request request, Response response)
    {
		timers.put(request.getCorrelationId(), new Timer());
    }

	@Override
    protected void onException(Throwable exception, Request request, Response response)
    {
		System.out.println(request.getRealMethod().toString() + " " + request.getUrl() + " responded with " + response.getStatus().toString());
		exception.printStackTrace();
    }

	@Override
    protected void onSuccess(Request request, Response response)
    {
    }

	@Override
    protected void onComplete(Request request, Response response)
    {
		Timer timer = timers.remove(request.getCorrelationId());
		
		if (timer != null)
		{
			timer.stop();
			System.out.println(request.getRealMethod().toString() + " " + request.getUrl() + " responded in " + timer.toString());
		}
		else
		{
			System.out.println(request.getRealMethod().toString() + " " + request.getUrl() + " with correlation ID " + request.getCorrelationId() + " had no timer.");			
		}
    }
	
	
	// SECTION: INNER CLASS
	
	private class Timer
	{
		private long startMillis = 0;
		private long stopMillis = 0;
		
		public Timer()
		{
			super();
			this.startMillis = System.currentTimeMillis();
		}
		
		public void stop()
		{
			this.stopMillis = System.currentTimeMillis();
		}
		
		public String toString()
		{
			long stopTime = (stopMillis == 0 ? System.currentTimeMillis() : stopMillis);

			return String.valueOf(stopTime - startMillis) + "ms";
		}
	}
}
