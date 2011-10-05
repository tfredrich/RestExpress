package com.blogging;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.blogging.postprocessor.EtagHeaderPostprocessor;
import com.blogging.serialization.BlogJsonProcessor;
import com.blogging.serialization.BlogXmlProcessor;
import com.strategicgains.repoexpress.exception.DuplicateItemException;
import com.strategicgains.repoexpress.exception.ItemNotFoundException;
import com.strategicgains.restexpress.Format;
import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.exception.BadRequestException;
import com.strategicgains.restexpress.exception.ConflictException;
import com.strategicgains.restexpress.exception.NotFoundException;
import com.strategicgains.restexpress.pipeline.SimpleConsoleLogMessageObserver;
import com.strategicgains.restexpress.plugin.RoutesMetadataPlugin;
import com.strategicgains.restexpress.postprocessor.CacheHeaderPostprocessor;
import com.strategicgains.restexpress.postprocessor.DateHeaderPostprocessor;
import com.strategicgains.restexpress.util.Environment;
import com.strategicgains.syntaxe.ValidationException;

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
		BloggingEnvironment env = loadEnvironment(args);
		RestExpress server = new RestExpress(new Routes(env.getBlogRespository(),
			env.getEntriesRespository(), env.getCommentsRespository()))
		    .setName(env.getName())
		    .setPort(env.getPort())
		    .putSerializationProcessor(Format.JSON, new BlogJsonProcessor())
		    .putSerializationProcessor(Format.XML, new BlogXmlProcessor())
		    .setDefaultFormat(env.getDefaultFormat())
		    .addMessageObserver(new SimpleConsoleLogMessageObserver())
		    .addPostprocessor(new DateHeaderPostprocessor())
		    .addPostprocessor(new CacheHeaderPostprocessor())
		    .addPostprocessor(new EtagHeaderPostprocessor());
		
		new RoutesMetadataPlugin().register(server);
		mapExceptions(server);
		server.bind();
		server.awaitShutdown();
	}

	/**
     * @param server
     */
    private static void mapExceptions(RestExpress server)
    {
    	server
    	.mapException(ItemNotFoundException.class, NotFoundException.class)
    	.mapException(DuplicateItemException.class, ConflictException.class)
    	.mapException(ValidationException.class, BadRequestException.class);
    }

	private static BloggingEnvironment loadEnvironment(String[] args)
    throws FileNotFoundException, IOException
    {
	    if (args.length > 0)
		{
			return Environment.from(args[0], BloggingEnvironment.class);
		}

	    return Environment.fromDefault(BloggingEnvironment.class);
    }
}
