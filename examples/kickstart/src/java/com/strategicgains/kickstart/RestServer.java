package com.strategicgains.kickstart;

import static com.strategicgains.restx.RestX.JSON_FORMAT;
import static com.strategicgains.restx.RestX.XML_FORMAT;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.bootstrap.ServerBootstrap;

import com.strategicgains.restx.domain.Link;
import com.strategicgains.restx.pipeline.DefaultRequestHandler;
import com.strategicgains.restx.pipeline.PipelineBuilder;
import com.strategicgains.restx.route.RouteResolver;
import com.strategicgains.restx.serialization.DefaultSerializationResolver;
import com.strategicgains.restx.serialization.SerializationProcessor;
import com.strategicgains.restx.serialization.json.DefaultJsonProcessor;
import com.strategicgains.restx.serialization.xml.DefaultXmlProcessor;
import com.strategicgains.restx.util.Bootstraps;
import com.strategicgains.restx.util.Resolver;
import com.thoughtworks.xstream.XStream;

/**
 * The main entry-point into RestX for the example services.
 * 
 * @author toddf
 * @since Aug 31, 2009
 */
public class RestServer
{
	private static final int DEFAULT_PORT = 3330;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		int port = DEFAULT_PORT;

		if (args.length > 0)
		{
			port = Integer.parseInt(args[0]);
		}

		// Configure the server.
		ServerBootstrap bootstrap = Bootstraps.createServerNioBootstrap();

		// Set up the event pipeline factory.
	    DefaultRequestHandler requestHandler = new DefaultRequestHandler(
	    	new RouteResolver(new Routes()),
	    	createSerializationResolver());
	    // Add pre/post processors to the request handler here...
//	    requestHandler.addPreprocessor(handler);
//	    requestHandler.addPostprocessor(handler);

	    PipelineBuilder pf = new PipelineBuilder()
			.setRequestHandler(requestHandler);
		bootstrap.setPipelineFactory(pf);

		// Bind and start to accept incoming connections.
		System.out.println("Starting RestX Example Server on port " + port);
		bootstrap.bind(new InetSocketAddress(port));
	}

	private static Resolver<SerializationProcessor> createSerializationResolver()
	{
		Map<String, SerializationProcessor> serializationProcessors = new HashMap<String, SerializationProcessor>();
		serializationProcessors.put(JSON_FORMAT, new DefaultJsonProcessor());

		serializationProcessors.put(XML_FORMAT, new DefaultXmlProcessor(
		    createXStream()));
		return new DefaultSerializationResolver(serializationProcessors, JSON_FORMAT);
	}

	private static XStream createXStream()
	{
		XStream xstream = new XStream();
		xstream.alias("link", Link.class);
		xstream.alias("list", Collections.EMPTY_LIST.getClass());
		return xstream;
	}
}
