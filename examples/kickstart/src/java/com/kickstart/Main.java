package com.kickstart;

import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.pipeline.SimpleMessageObserver;

/**
 * The main entry-point into RestExpress for the example services.
 * 
 * @author toddf
 * @since Aug 31, 2009
 */
public class Main
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		RestExpress server = new RestExpress(new Routes())
		    .setName("KickStart Example")
		    .supportConsoleRoutes()
//		    .useRawResponses()
		    .addMessageObserver(new SimpleMessageObserver());
		configureXmlAliases(server);
		server.bind();
		server.awaitShutdown();
	}

	private static void configureXmlAliases(RestExpress server)
	{
		// server
		// .alias("element_name", Element.class)
		// .alias("element_name", Element.class)
		// .alias("element_name", Element.class)
		// .alias("element_name", Element.class)
	}
}
