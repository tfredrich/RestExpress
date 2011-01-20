package com.kickstart;

import com.strategicgains.restexpress.Format;
import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.pipeline.SimpleMessageObserver;
import com.strategicgains.restexpress.serialization.SerializationProcessor;
import com.strategicgains.restexpress.serialization.xml.DefaultXmlProcessor;

/**
 * The main entry-point into RestExpress for the example services.
 * 
 * @author toddf
 * @since Aug 31, 2009
 */
public class Main
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

		RestExpress server = new RestExpress(new Routes())
			.setName("KickStart Example")
			.setPort(port)
			.putSerializationProcessor(Format.XML, createXmlProcessor())
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
