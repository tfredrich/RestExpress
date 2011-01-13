package com.kickstart;

import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.RestExpressServer;
import com.strategicgains.restexpress.pipeline.SimpleMessageObserver;
import com.strategicgains.restexpress.serialization.SerializationProcessor;
import com.strategicgains.restexpress.serialization.xml.DefaultXmlProcessor;

/**
 * The main entry-point into RestExpress for the example services.
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

		RestExpressServer server = new RestExpressServer("KickStart Example")
			.setPort(port)
			.setRoutes(new Routes())
			.putSerializationProcessor(RestExpress.XML_FORMAT, createXmlProcessor())
			.addMessageObserver(new SimpleMessageObserver());
		server.bind();
	}

	private static SerializationProcessor createXmlProcessor()
	{
		DefaultXmlProcessor xmlProcessor = new DefaultXmlProcessor();
//		xmlProcessor.alias("element_name", Element.class);
//		...
		return xmlProcessor;
	}
}
