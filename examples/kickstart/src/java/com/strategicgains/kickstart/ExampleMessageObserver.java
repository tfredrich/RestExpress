package com.strategicgains.kickstart;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.pipeline.MessageObserver;

/**
 * @author toddf
 * @since Dec 16, 2010
 */
public class ExampleMessageObserver
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
