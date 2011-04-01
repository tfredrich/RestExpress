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
import static org.junit.Assert.assertTrue;

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

import com.strategicgains.restexpress.Format;
import com.strategicgains.restexpress.response.DefaultResponseWrapper;
import com.strategicgains.restexpress.response.StringBufferHttpResponseWriter;
import com.strategicgains.restexpress.route.RouteDeclaration;
import com.strategicgains.restexpress.route.RouteResolver;
import com.strategicgains.restexpress.serialization.DefaultSerializationResolver;
import com.strategicgains.restexpress.serialization.json.DefaultJsonProcessor;
import com.strategicgains.restexpress.serialization.xml.DefaultXmlProcessor;


/**
 * @author toddf
 * @since Dec 15, 2010
 */
public class JsendWrappedResponseTest
{
	private DefaultRequestHandler messageHandler;
	private WrappedResponseObserver observer;
	private Channel channel;
    private ChannelPipeline pl;
    private StringBuffer httpResponse;
	
	@Before
	public void initialize()
	throws Exception
	{
		DefaultSerializationResolver resolver = new DefaultSerializationResolver();
		resolver.put(Format.JSON, new DefaultJsonProcessor());
		resolver.put(Format.XML, new DefaultXmlProcessor());
		resolver.setDefaultFormat(Format.JSON);
		
		messageHandler = new DefaultRequestHandler(new RouteResolver(new DummyRoutes().createRouteMapping()), resolver);
		observer = new WrappedResponseObserver();
		messageHandler.addMessageObserver(observer);
		messageHandler.setResponseWrapperFactory(new DefaultResponseWrapper());
		httpResponse = new StringBuffer();
		messageHandler.setResponseWriter(new StringBufferHttpResponseWriter(httpResponse));
		PipelineBuilder pf = new PipelineBuilder()
			.addRequestHandler(messageHandler);
	    pl = pf.getPipeline();
	    ChannelFactory channelFactory = new DefaultLocalServerChannelFactory();
	    channel = channelFactory.newChannel(pl);
	}

	@Test
	public void shouldWrapGetInJsendJson()
	{
		sendEvent(HttpMethod.GET, "/normal_get.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("{\"code\":200,\"status\":\"success\",\"data\":\"Normal GET action\"}", httpResponse.toString());
	}

	@Test
	public void shouldWrapGetInJsendJsonUsingQueryString()
	{
		sendEvent(HttpMethod.GET, "/normal_get?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("{\"code\":200,\"status\":\"success\",\"data\":\"Normal GET action\"}", httpResponse.toString());
	}

	@Test
	public void shouldWrapGetInJsendXml()
	{
		sendEvent(HttpMethod.GET, "/normal_get.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertTrue(httpResponse.toString().startsWith("<response>"));
		assertTrue(httpResponse.toString().contains("<code>200</code>"));
		assertTrue(httpResponse.toString().contains("<status>success</status>"));
		assertTrue(httpResponse.toString().contains("<data class=\"string\">Normal GET action</data>"));
		assertTrue(httpResponse.toString().endsWith("</response>"));
	}

	@Test
	public void shouldWrapGetInJsendXmlUsingQueryString()
	{
		sendEvent(HttpMethod.GET, "/normal_get?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertTrue(httpResponse.toString().startsWith("<response>"));
		assertTrue(httpResponse.toString().contains("<code>200</code>"));
		assertTrue(httpResponse.toString().contains("<status>success</status>"));
		assertTrue(httpResponse.toString().contains("<data class=\"string\">Normal GET action</data>"));
		assertTrue(httpResponse.toString().endsWith("</response>"));
	}

	@Test
	public void shouldWrapPutInJsendJson()
	{
		sendEvent(HttpMethod.PUT, "/normal_put.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("{\"code\":200,\"status\":\"success\",\"data\":\"Normal PUT action\"}", httpResponse.toString());
	}

	@Test
	public void shouldWrapPutInJsendJsonUsingQueryString()
	{
		sendEvent(HttpMethod.PUT, "/normal_put?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("{\"code\":200,\"status\":\"success\",\"data\":\"Normal PUT action\"}", httpResponse.toString());
	}

	@Test
	public void shouldWrapPutInJsendXml()
	{
		sendEvent(HttpMethod.PUT, "/normal_put.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertTrue(httpResponse.toString().startsWith("<response>"));
		assertTrue(httpResponse.toString().contains("<code>200</code>"));
		assertTrue(httpResponse.toString().contains("<status>success</status>"));
		assertTrue(httpResponse.toString().contains("<data class=\"string\">Normal PUT action</data>"));
		assertTrue(httpResponse.toString().endsWith("</response>"));
	}

	@Test
	public void shouldWrapPutInJsendXmlUsingQueryString()
	{
		sendEvent(HttpMethod.PUT, "/normal_put?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertTrue(httpResponse.toString().startsWith("<response>"));
		assertTrue(httpResponse.toString().contains("<code>200</code>"));
		assertTrue(httpResponse.toString().contains("<status>success</status>"));
		assertTrue(httpResponse.toString().contains("<data class=\"string\">Normal PUT action</data>"));
		assertTrue(httpResponse.toString().endsWith("</response>"));
	}

	@Test
	public void shouldWrapPostInJsendJson()
	{
		sendEvent(HttpMethod.POST, "/normal_post.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("{\"code\":200,\"status\":\"success\",\"data\":\"Normal POST action\"}", httpResponse.toString());
	}

	@Test
	public void shouldWrapPostInJsendJsonUsingQueryString()
	{
		sendEvent(HttpMethod.POST, "/normal_post?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("{\"code\":200,\"status\":\"success\",\"data\":\"Normal POST action\"}", httpResponse.toString());
	}

	@Test
	public void shouldWrapPostInJsendXml()
	{
		sendEvent(HttpMethod.POST, "/normal_post.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertTrue(httpResponse.toString().startsWith("<response>"));
		assertTrue(httpResponse.toString().contains("<code>200</code>"));
		assertTrue(httpResponse.toString().contains("<status>success</status>"));
		assertTrue(httpResponse.toString().contains("<data class=\"string\">Normal POST action</data>"));
		assertTrue(httpResponse.toString().endsWith("</response>"));
	}

	@Test
	public void shouldWrapPostInJsendXmlUsingQueryString()
	{
		sendEvent(HttpMethod.POST, "/normal_post?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertTrue(httpResponse.toString().startsWith("<response>"));
		assertTrue(httpResponse.toString().contains("<code>200</code>"));
		assertTrue(httpResponse.toString().contains("<status>success</status>"));
		assertTrue(httpResponse.toString().contains("<data class=\"string\">Normal POST action</data>"));
		assertTrue(httpResponse.toString().endsWith("</response>"));
	}

	@Test
	public void shouldWrapDeleteInJsendJson()
	{
		sendEvent(HttpMethod.DELETE, "/normal_delete.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("{\"code\":200,\"status\":\"success\",\"data\":\"Normal DELETE action\"}", httpResponse.toString());
	}

	@Test
	public void shouldWrapDeleteInJsendJsonUsingQueryString()
	{
		sendEvent(HttpMethod.DELETE, "/normal_delete?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("{\"code\":200,\"status\":\"success\",\"data\":\"Normal DELETE action\"}", httpResponse.toString());
	}

	@Test
	public void shouldWrapDeleteInJsendXml()
	{
		sendEvent(HttpMethod.DELETE, "/normal_delete.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertTrue(httpResponse.toString().startsWith("<response>"));
		assertTrue(httpResponse.toString().contains("<code>200</code>"));
		assertTrue(httpResponse.toString().contains("<status>success</status>"));
		assertTrue(httpResponse.toString().contains("<data class=\"string\">Normal DELETE action</data>"));
		assertTrue(httpResponse.toString().endsWith("</response>"));
	}

	@Test
	public void shouldWrapDeleteInJsendXmlUsingQueryString()
	{
		sendEvent(HttpMethod.DELETE, "/normal_delete?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(1, observer.getSuccessCount());
		assertEquals(0, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertTrue(httpResponse.toString().startsWith("<response>"));
		assertTrue(httpResponse.toString().contains("<code>200</code>"));
		assertTrue(httpResponse.toString().contains("<status>success</status>"));
		assertTrue(httpResponse.toString().contains("<data class=\"string\">Normal DELETE action</data>"));
		assertTrue(httpResponse.toString().endsWith("</response>"));
	}

	@Test
	public void shouldWrapNotFoundInJsendJson()
	{
		sendEvent(HttpMethod.GET, "/not_found.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("{\"code\":404,\"status\":\"error\",\"message\":\"Item not found\",\"data\":\"NotFoundException\"}", httpResponse.toString());
	}

	@Test
	public void shouldWrapNotFoundInJsendJsonUsingQueryString()
	{
		sendEvent(HttpMethod.GET, "/not_found?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("{\"code\":404,\"status\":\"error\",\"message\":\"Item not found\",\"data\":\"NotFoundException\"}", httpResponse.toString());
	}

	@Test
	public void shouldWrapNotFoundInJsendXml()
	{
		sendEvent(HttpMethod.GET, "/not_found.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertTrue(httpResponse.toString().startsWith("<response>"));
		assertTrue(httpResponse.toString().contains("<code>404</code>"));
		assertTrue(httpResponse.toString().contains("<status>error</status>"));
		assertTrue(httpResponse.toString().contains("<message>Item not found</message>"));
		assertTrue(httpResponse.toString().contains("<data class=\"string\">NotFoundException</data>"));
		assertTrue(httpResponse.toString().endsWith("</response>"));
	}

	@Test
	public void shouldWrapNotFoundInJsendXmlUsingQueryString()
	{
		sendEvent(HttpMethod.GET, "/not_found?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertTrue(httpResponse.toString().startsWith("<response>"));
		assertTrue(httpResponse.toString().contains("<code>404</code>"));
		assertTrue(httpResponse.toString().contains("<status>error</status>"));
		assertTrue(httpResponse.toString().contains("<message>Item not found</message>"));
		assertTrue(httpResponse.toString().endsWith("</response>"));
	}

	@Test
	public void shouldWrapNullPointerInJsendJson()
	{
		sendEvent(HttpMethod.GET, "/null_pointer.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("{\"code\":500,\"status\":\"fail\",\"message\":\"Null and void\",\"data\":\"NullPointerException\"}", httpResponse.toString());
	}

	@Test
	public void shouldWrapNullPointerInJsendJsonUsingQueryString()
	{
		sendEvent(HttpMethod.GET, "/null_pointer?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("{\"code\":500,\"status\":\"fail\",\"message\":\"Null and void\",\"data\":\"NullPointerException\"}", httpResponse.toString());
	}

	@Test
	public void shouldWrapNullPointerInJsendXml()
	{
		sendEvent(HttpMethod.GET, "/null_pointer.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertTrue(httpResponse.toString().startsWith("<response>"));
		assertTrue(httpResponse.toString().contains("<code>500</code>"));
		assertTrue(httpResponse.toString().contains("<status>fail</status>"));
		assertTrue(httpResponse.toString().contains("<message>Null and void</message>"));
		assertTrue(httpResponse.toString().contains("<data class=\"string\">NullPointerException</data>"));
		assertTrue(httpResponse.toString().endsWith("</response>"));
	}

	@Test
	public void shouldWrapNullPointerInJsendXmlUsingQueryString()
	{
		sendEvent(HttpMethod.GET, "/null_pointer?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertTrue(httpResponse.toString().startsWith("<response>"));
		assertTrue(httpResponse.toString().contains("<code>500</code>"));
		assertTrue(httpResponse.toString().contains("<status>fail</status>"));
		assertTrue(httpResponse.toString().contains("<message>Null and void</message>"));
		assertTrue(httpResponse.toString().contains("<data class=\"string\">NullPointerException</data>"));
		assertTrue(httpResponse.toString().endsWith("</response>"));
	}

	@Test
	public void shouldWrapInvalidUrlWithJsonFormat()
	{
		sendEvent(HttpMethod.GET, "/xyzt.json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("{\"code\":404,\"status\":\"error\",\"message\":\"Unresolvable URL: http://null/xyzt.json\",\"data\":\"NotFoundException\"}", httpResponse.toString());
	}

	@Test
	public void shouldWrapInvalidUrlWithJsonFormatUsingQueryString()
	{
		sendEvent(HttpMethod.GET, "/xyzt?format=json", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertEquals("{\"code\":404,\"status\":\"error\",\"message\":\"Unresolvable URL: http://null/xyzt?format=json\",\"data\":\"NotFoundException\"}", httpResponse.toString());
	}

	@Test
	public void shouldWrapInvalidUrlWithXmlFormat()
	{
		sendEvent(HttpMethod.GET, "/xyzt.xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertTrue(httpResponse.toString().startsWith("<response>"));
		assertTrue(httpResponse.toString().contains("<code>404</code>"));
		assertTrue(httpResponse.toString().contains("<status>error</status>"));
		assertTrue(httpResponse.toString().contains("<message>Unresolvable URL: http://null/xyzt.xml</message>"));
		assertTrue(httpResponse.toString().endsWith("</response>"));
	}

	@Test
	public void shouldWrapInvalidUrlWithXmlFormatUsingQueryString()
	{
		sendEvent(HttpMethod.GET, "/xyzt?format=xml", null);
		assertEquals(1, observer.getReceivedCount());
		assertEquals(1, observer.getCompleteCount());
		assertEquals(0, observer.getSuccessCount());
		assertEquals(1, observer.getExceptionCount());
//		System.out.println(httpResponse.toString());
		assertTrue(httpResponse.toString().startsWith("<response>"));
		assertTrue(httpResponse.toString().contains("<code>404</code>"));
		assertTrue(httpResponse.toString().contains("<status>error</status>"));
		assertTrue(httpResponse.toString().contains("<message>Unresolvable URL: http://null/xyzt?format=xml</message>"));
		assertTrue(httpResponse.toString().endsWith("</response>"));
	}

	private void sendEvent(HttpMethod method, String path, String body)
    {
		HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, method, path);
		
		if (body != null)
		{
			request.setContent(ChannelBuffers.copiedBuffer(body, Charset.defaultCharset()));
		}

	    pl.sendUpstream(new UpstreamMessageEvent(
	    	channel,
	    	request,
	    	new InetSocketAddress(1)));
    }
	
	public class DummyRoutes
	extends RouteDeclaration
	{
		private Object controller = new WrappedResponseController();

        @Override
        protected void defineRoutes()
        {
        	uri("/normal_get.{format}", controller)
        		.action("normalGetAction", HttpMethod.GET);

        	uri("/normal_put.{format}", controller)
    		.action("normalPutAction", HttpMethod.PUT);

        	uri("/normal_post.{format}", controller)
    		.action("normalPostAction", HttpMethod.POST);

        	uri("/normal_delete.{format}", controller)
    		.action("normalDeleteAction", HttpMethod.DELETE);

        	uri("/not_found.{format}", controller)
        		.action("notFoundAction", HttpMethod.GET);

        	uri("/null_pointer.{format}", controller)
        		.action("nullPointerAction", HttpMethod.GET);
        }
	}
}
