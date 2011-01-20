/*
 * Copyright 2009, Strategic Gains, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.strategicgains.restexpress;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;

import com.strategicgains.restexpress.pipeline.DefaultRequestHandler;
import com.strategicgains.restexpress.pipeline.MessageObserver;
import com.strategicgains.restexpress.pipeline.PipelineBuilder;
import com.strategicgains.restexpress.pipeline.Postprocessor;
import com.strategicgains.restexpress.pipeline.Preprocessor;
import com.strategicgains.restexpress.route.RouteDeclaration;
import com.strategicgains.restexpress.route.RouteResolver;
import com.strategicgains.restexpress.serialization.DefaultSerializationResolver;
import com.strategicgains.restexpress.serialization.SerializationProcessor;
import com.strategicgains.restexpress.util.Bootstraps;
import com.strategicgains.restexpress.util.Resolver;

/**
 * Primary entry point to create a RestExpress service.  All that's required is a RouteDeclaration.
 * The default port is 8081.
 *   
 * @author toddf
 */
public class RestExpress
{
	private static final int DEFAULT_PORT = 8081;

	private String name;
	private int port;
	private RouteDeclaration routes;
	Map<String, SerializationProcessor> serializationProcessors = new HashMap<String, SerializationProcessor>();
	private Resolver<SerializationProcessor> serializationResolver;
	private String defaultSerializationFormat = Format.JSON;
	private List<MessageObserver> messageObservers = new ArrayList<MessageObserver>();
	private List<Preprocessor> preprocessors = new ArrayList<Preprocessor>();
	private List<Postprocessor> postprocessors = new ArrayList<Postprocessor>();
	private boolean useSystemOut = true;
	
	public RestExpress(RouteDeclaration routes)
	{
		super();
		setRoutes(routes);
		setPort(DEFAULT_PORT);
	}
	
	public String getName()
	{
		return name;
	}
	
	public RestExpress setName(String name)
	{
		this.name = name;
		return this;
	}

	public int getPort()
	{
		return port;
	}

	public RestExpress setPort(int port)
	{
		this.port = port;
		return this;
	}

	public RouteDeclaration getRoutes()
	{
		return routes;
	}

	private RestExpress setRoutes(RouteDeclaration routes)
	{
		this.routes = routes;
		return this;
	}

	public RestExpress putSerializationProcessor(String format, SerializationProcessor processor)
	{
		serializationProcessors.put(format, processor);
		return this;
	}

	public Map<String, SerializationProcessor> getSerializationProcessors()
	{
		return serializationProcessors;
	}

	public Resolver<SerializationProcessor> getSerializationResolver()
	{
		return serializationResolver;
	}

	public RestExpress setSerializationResolver(Resolver<SerializationProcessor> serializationResolver)
	{
		this.serializationResolver = serializationResolver;
		return this;
	}

	public String getDefaultSerializationFormat()
	{
		return defaultSerializationFormat;
	}

	public RestExpress setDefaultSerializationFormat(String defaultSerializationFormat)
	{
		this.defaultSerializationFormat = defaultSerializationFormat;
		return this;
	}

	public RestExpress addMessageObserver(MessageObserver observer)
	{
		if(!messageObservers.contains(observer))
		{
			messageObservers.add(observer);
		}
		
		return this;
	}

	public List<MessageObserver> getMessageObservers()
	{
		return messageObservers;
	}

	public RestExpress addPreprocessor(Preprocessor processor)
	{
		if (!preprocessors.contains(processor))
		{
			preprocessors.add(processor);
		}
		
		return this;
	}

	public List<Preprocessor> getPreprocessors()
	{
		return preprocessors;
	}

	public RestExpress addPostprocessor(Postprocessor processor)
	{
		if(!postprocessors.contains(processor))
		{
			postprocessors.add(processor);
		}
		
		return this;
	}

	public List<Postprocessor> getPostprocessors()
	{
		return postprocessors;
	}

	public boolean useSystemOut()
	{
		return useSystemOut;
	}

	public RestExpress setUseSystemOut(boolean useSystemOut)
	{
		this.useSystemOut = useSystemOut;
		return this;
	}
	
	public Channel bind()
	{
		// Configure the server.
		ServerBootstrap bootstrap = Bootstraps.createServerNioBootstrap();

		// Set up the event pipeline factory.
	    DefaultRequestHandler requestHandler = new DefaultRequestHandler(
	    	createRouteResolver(),
	    	createSerializationResolver());
	    
	    // Add MessageObservers to the request handler here, if desired...
	    requestHandler.addMessageObserver();

	    // Add pre/post processors to the request handler here...
	    addPrerocessors(requestHandler);
	    addPostprocessors(requestHandler);

	    PipelineBuilder pf = new PipelineBuilder()
			.setRequestHandler(requestHandler);
		bootstrap.setPipelineFactory(pf);

		// Bind and start to accept incoming connections.
		if(useSystemOut())
		{
			System.out.println("Starting " + getName() + " Server on port " + port);
		}

		return bootstrap.bind(new InetSocketAddress(getPort()));
	}

	/**
     * @return
     */
    private RouteResolver createRouteResolver()
    {
	    return new RouteResolver(getRoutes().createRouteMapping());
    }

	/**
     * @return
     */
    private Resolver<SerializationProcessor> createSerializationResolver()
    {
	    DefaultSerializationResolver resolver = new DefaultSerializationResolver();
	    
	    for (Entry<String, SerializationProcessor> entry : getSerializationProcessors().entrySet())
	    {
	    	resolver.put(entry.getKey(), entry.getValue());
	    }

	    return resolver;
    }

	/**
     * @param requestHandler
     */
    private void addPrerocessors(DefaultRequestHandler requestHandler)
    {
    	for (Preprocessor processor : getPreprocessors())
    	{
    		requestHandler.addPreprocessor(processor);
    	}
    }

	/**
     * @param requestHandler
     */
    private void addPostprocessors(DefaultRequestHandler requestHandler)
    {
    	for (Postprocessor processor : getPostprocessors())
    	{
    		requestHandler.addPostprocessor(processor);
    	}
    }
}
