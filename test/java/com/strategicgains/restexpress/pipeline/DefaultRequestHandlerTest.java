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

import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.UpstreamMessageEvent;
import org.jboss.netty.channel.local.DefaultLocalServerChannelFactory;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.junit.Before;
import org.junit.Test;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.exception.BadRequestException;
import com.strategicgains.restexpress.route.RouteResolver;
import com.strategicgains.restexpress.route.RouteDeclaration;
import com.strategicgains.restexpress.serialization.DefaultSerializationResolver;


/**
 * @author toddf
 * @since Dec 15, 2010
 */
public class DefaultRequestHandlerTest
{
	private DefaultRequestHandler messageHandler;
	private DummyObserver observer;
	private Channel channel;
    private ChannelPipeline pl;
	
	@Before
	public void initialize()
	throws Exception
	{
		messageHandler = new DefaultRequestHandler(new RouteResolver(new DummyRoutes().createRouteMapping()),
			new DefaultSerializationResolver());
		observer = new DummyObserver();
		messageHandler.addMessageObserver(observer);
		PipelineBuilder pf = new PipelineBuilder()
			.setRequestHandler(messageHandler);
	    pl = pf.getPipeline();
	    ChannelFactory channelFactory = new DefaultLocalServerChannelFactory();
	    channel = channelFactory.newChannel(pl);
	}

	@Test
	public void shouldNotifyObserverOnSuccess()
	throws Exception
	{
		sendGetEvent("/foo");
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
	}

	@Test
	public void shouldNotifyObserverOnError()
	throws Exception
	{
		sendGetEvent("/bar");
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getExceptionCount());
		assertEquals(0, observer.getSuccessCount());
	}

	@Test
	public void shouldParseTimepointJson()
	{
		sendGetEvent("/date.json", "{\"at\":\"2010-12-17T120000Z\"}");
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
	}

	@Test
	public void shouldParseTimepointXml()
	{
		sendGetEvent("/date.xml", "<com.strategicgains.restexpress.pipeline.Dated><at>2010-12-17T120000Z</at></com.strategicgains.restexpress.pipeline.Dated>");
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
	}

	private void sendGetEvent(String path)
    {
	    pl.sendUpstream(new UpstreamMessageEvent(
	    	channel,
	    	new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path),
	    	new InetSocketAddress(1)));
    }

	private void sendGetEvent(String path, String body)
    {
		HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path);
		request.setContent(ChannelBuffers.copiedBuffer(body, Charset.defaultCharset()));

	    pl.sendUpstream(new UpstreamMessageEvent(
	    	channel,
	    	request,
	    	new InetSocketAddress(1)));
    }
	
	public class DummyRoutes
	extends RouteDeclaration
	{
		private Object controller = new FooBarController();

        @Override
        protected void defineRoutes()
        {
        	uri("/foo", controller)
        		.action("fooAction", HttpMethod.GET);

        	uri("/bar", controller)
        		.action("barAction", HttpMethod.GET);

        	uri("/date.{format}", controller)
    			.action("dateAction", HttpMethod.GET);
        }
	}
	
	public class FooBarController
	{
		public void fooAction(Request request, Response response)
		{
			// do nothing.
		}
		
		public void barAction(Request request, Response response)
		{
			throw new BadRequestException("foobar'd");
		}
		
		public Object dateAction(Request request, Response response)
		{
			return request.getBodyAs(Dated.class);
		}
	}

	public class DummyObserver
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
        protected void onException(Throwable exception, Request request,
            Response response)
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
}
