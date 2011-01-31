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
import com.strategicgains.restexpress.serialization.AliasingSerializationProcessor;
import com.strategicgains.restexpress.serialization.DefaultSerializationResolver;
import com.strategicgains.restexpress.serialization.SerializationProcessor;
import com.strategicgains.restexpress.serialization.json.DefaultJsonProcessor;
import com.strategicgains.restexpress.serialization.text.DefaultTxtProcessor;
import com.strategicgains.restexpress.serialization.xml.DefaultXmlProcessor;
import com.strategicgains.restexpress.util.Bootstraps;
import com.strategicgains.restexpress.util.Resolver;

/**
 * Primary entry point to create a RestExpress service.  All that's required is a RouteDeclaration.
 * By default: port is 8081, serialization format is JSON, supported formats are JSON and XML.
 *   
 * @author toddf
 */
public class RestExpress
{
	private static final int DEFAULT_PORT = 8081;
	private static final String DEFAULT_NAME = "RestExpress";
	private static final String DEFAULT_CONSOLE_PREFIX = "/console";

	private String name;
	private int port;
	private RouteDeclaration routes;
	private String defaultFormat;
	private boolean useSystemOut;
	private boolean useConsoleRoutes;
	private String consoleUrlPrefix;
	
	Map<String, SerializationProcessor> serializationProcessors = new HashMap<String, SerializationProcessor>();
	private List<MessageObserver> messageObservers = new ArrayList<MessageObserver>();
	private List<Preprocessor> preprocessors = new ArrayList<Preprocessor>();
	private List<Postprocessor> postprocessors = new ArrayList<Postprocessor>();
	private Map<String, Class<?>> xmlAliases = new HashMap<String, Class<?>>();
	private Resolver<SerializationProcessor> serializationResolver;
	
	/**
	 * Create a new RestExpress service.  By default, RestExpress uses port 8081.  Supports JSON, and XML.
	 * And displays some messages on System.out.  These can be altered with the setPort(), noJson(), noXml(),
	 * noSystemOut() DSL modifiers, as needed.
	 * 
	 * <p/>The default input and output format for messages is JSON.  To change that, use the
	 * setDefaultFormat(String) DSL modifier, passing the format to use by default.  Make sure there's
	 * a corresponding SerializationProcessor for that format.  The Format class has the basics.
	 * 
	 * <p/>This DSL was created as a thin veneer on Netty functionality.  The bind() method simply builds a
	 * Netty pipeline and uses this builder class to create it.  Underneath the covers, RestExpress uses
	 * Google GSON for JSON handling and XStream for XML processing.  However, both of those can be swapped
	 * out using the putSerializationProcessor(String, SerializationProcessor) method, creating your own
	 * instance of SerializationProcessor as necessary.
	 * 
	 * @param routes a RouteDeclaration that declares the URL routes that this service supports.
	 */
	public RestExpress(RouteDeclaration routes)
	{
		super();
		setRoutes(routes);
		setName(DEFAULT_NAME);
		setPort(DEFAULT_PORT);
		supportJson(true);
		supportXml();
		supportConsoleRoutes();
		useSystemOut();
	}

	/**
	 * Get the name of this RestExpress service.
	 * 
	 * @return a String representing the name of this service suite.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Set the name of this RestExpress service suite.
	 * 
	 * @param name the name.
	 * @return the RestExpress instance to facilitate DSL-style method chaining.
	 */
	public RestExpress setName(String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * Get the port that this RestExpress service suite is listening on.
	 *  
	 * @return the HTTP port.
	 */
	public int getPort()
	{
		return port;
	}

	/**
	 * Set the port that this RestExpress service suite should listen on.
	 * The default is 8081.
	 * 
	 * @param port the HTTP port RestExpress will listen on.
	 * @return the RestExpress instance to facilitate DSL-style method chaining.
	 */
	public RestExpress setPort(int port)
	{
		this.port = port;
		return this;
	}

	public RouteDeclaration getRoutes()
	{
		return routes;
	}

	/**
	 * Set the routes (URLs) that this RestExpress service suite supports.
	 * 
	 * @param routes a RouteDeclaration
	 * @return the RestExpress instance to facilitate DSL-style method chaining.
	 */
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

	public String getDefaultFormat()
	{
		return defaultFormat;
	}

	public RestExpress setDefaultFormat(String format)
	{
		this.defaultFormat = format;
		return this;
	}

	/**
	 * Tell RestExpress to support JSON in routes, incoming and outgoing.
	 * By default RestExpress supports JSON and is the default.
	 *
	 * @param isDefault true to make JSON the default format.
	 * @return the RestExpress instance.
	 */
	public RestExpress supportJson(boolean isDefault)
	{
		if (! getSerializationProcessors().containsKey(Format.JSON))
		{
			serializationProcessors.put(Format.JSON, new DefaultJsonProcessor());
		}

		if (isDefault)
		{
			setDefaultFormat(Format.JSON);
		}

		return this;
	}

	/**
	 * Tell RestExpress to support JSON in routes, incoming and outgoing.
	 * By default RestExpress supports JSON and is the default.
	 * 
	 * @return the RestExpress instance.
	 */
	public RestExpress supportJson()
	{
		return supportJson(false);
	}

	/**
	 * Tell RestExpress to not support JSON in routes, incoming or outgoing.
	 * Client must call setDefaultFormat(String) to set the default format to something else.
	 * 
	 * @return the RestExpress instance.
	 */
	public RestExpress noJson()
	{
		serializationProcessors.remove(Format.JSON);
		return this;
	}

	/**
	 * Tell RestExpress to support XML in routes, incoming and outgoing.
	 * By default RestExpress supports XML.
	 *
	 * @param isDefault true to make XML the default format.
	 * @return the RestExpress instance.
	 */
	public RestExpress supportXml(boolean isDefault)
	{
		if (!getSerializationProcessors().containsKey(Format.XML))
		{
			getSerializationProcessors().put(Format.XML, new DefaultXmlProcessor());
		}
		
		if (isDefault)
		{
			setDefaultFormat(Format.XML);
		}
		
		return this;
	}
	
	/**
	 * Tell RestExpress to support XML in routes, incoming and outgoing.
	 * By default RestExpress supports XML.
	 *
	 * @param isDefault true to make XML the default format.
	 * @return the RestExpress instance.
	 */
	public RestExpress supportXml()
	{
		return supportXml(false);
	}
	
	/**
	 * Tell RestExpress to not support XML in routes, incoming or outgoing.
	 * 
	 * @return the RestExpress instance.
	 */
	public RestExpress noXml()
	{
		serializationProcessors.remove(Format.XML);
		return this;
	}

	/**
	 * Tell RestExpress to support TXT format specifiers in routes, outgoing only at present.
	 *
	 * @param isDefault true to make TXT the default format.
	 * @return the RestExpress instance.
	 */
	public RestExpress supportTxt(boolean isDefault)
	{
		if (!getSerializationProcessors().containsKey(Format.TXT))
		{
			getSerializationProcessors().put(Format.TXT, new DefaultTxtProcessor());
		}

		if (isDefault)
		{
			setDefaultFormat(Format.TXT);
		}

		return this;
	}

	/**
	 * Tell RestExpress to support TXT format specifier in routes, outgoing only at present.
	 * 
	 * @return the RestExpress instance.
	 */
	public RestExpress supportTxt()
	{
		return supportTxt(false);
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

	/**
	 * Add a Preprocessor instance that gets called before an incoming message gets processed.
	 * Preprocessors get called in the order in which they are added.  To break out of the chain,
	 * simply throw an exception.
	 * 
	 * @param processor
	 * @return
	 */
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

	/**
	 * Add a PostProcessor instance that gets call after an incoming message is processed.  A 
	 * Postprocessor is useful for augmenting or transforming the results.  Postprocessors get
	 * called in the order in which they get added.
	 * 
	 * @param processor
	 * @return
	 */
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

	public boolean shouldUseSystemOut()
	{
		return useSystemOut;
	}

	public RestExpress setUseSystemOut(boolean useSystemOut)
	{
		this.useSystemOut = useSystemOut;
		return this;
	}
	
	public RestExpress useSystemOut()
	{
		setUseSystemOut(true);
		return this;
	}
	
	public RestExpress noSystemOut()
	{
		setUseSystemOut(false);
		return this;
	}
	
	/**
	 * 
	 * @param elementName
	 * @param theClass
	 * @return
	 */
	public RestExpress alias(String elementName, Class<?> theClass)
	{
		xmlAliases.put(elementName, theClass);
		return this;
	}
	
	public RestExpress supportConsoleRoutes()
	{
		return supportConsoleRoutes(DEFAULT_CONSOLE_PREFIX);
	}
	
	public RestExpress supportConsoleRoutes(String urlPrefix)
	{
		useConsoleRoutes = true;
		this.consoleUrlPrefix = urlPrefix;
		return this;
	}
	
	/**
	 * The last call in the building of a RestExpress server, bind() causes Netty to bind to the
	 * listening address and process incoming messages.
	 * 
	 * @return Channel
	 */
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
		if(shouldUseSystemOut())
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
	    resolver.setDefaultFormat(getDefaultFormat());
	    
	    for (Entry<String, SerializationProcessor> entry : getSerializationProcessors().entrySet())
	    {
	    	if (entry.getKey().equals(Format.XML))
	    	{
	    		setXmlAliases((AliasingSerializationProcessor) entry.getValue());
	    	}

	    	resolver.put(entry.getKey(), entry.getValue());
	    }

	    return resolver;
    }

	/**
     * @param value
     */
    private void setXmlAliases(AliasingSerializationProcessor value)
    {
    	for (Entry<String, Class<?>> entry : xmlAliases.entrySet())
    	{
    		value.alias(entry.getKey(), entry.getValue());
    	}
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
