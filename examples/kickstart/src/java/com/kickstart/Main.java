package com.kickstart;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.pipeline.SimpleMessageObserver;
import com.strategicgains.restexpress.util.Environment;

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
	public static void main(String[] args) throws Exception
	{
		KickstartEnvironment env = loadEnvironment(args);
		RestExpress server = new RestExpress(new Routes())
		    .setName(env.getName())
		    .setPort(env.getPort())
		    .supportConsoleRoutes()
		    // .useRawResponses()
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

	private static KickstartEnvironment loadEnvironment(String[] args)
    throws FileNotFoundException, IOException
    {
	    if (args.length > 0)
		{
			return Environment.from(args[0], KickstartEnvironment.class);
		}

	    return Environment.fromDefault(KickstartEnvironment.class);
    }
}
