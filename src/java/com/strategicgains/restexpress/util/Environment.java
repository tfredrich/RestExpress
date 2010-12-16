/*
 * Copyright 2010, eCollege, Inc.  All rights reserved.
 */
package com.strategicgains.restexpress.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author toddf
 * @since Dec 16, 2010
 */
public abstract class Environment
{
//	private static final Logger log = LoggerFactory.getLogger(Environment.class);

	public static <T extends Environment> T from(String environmentName, Class<T> type)
	throws FileNotFoundException, IOException
	{
		T instance = newEnvironment(type);

		if (environmentName != null)
		{
			instance.load("config/" + environmentName + "/environment.properties");
		}

		return instance;
	}

	protected void load(String environmentFile)
	throws FileNotFoundException, IOException
	{
//		log.debug("loading environment properties from " + environmentFile);
		Properties p = readProperties(environmentFile);
		fillValues(p);
	}
	
	protected abstract void fillValues(Properties p);

	protected Properties readProperties(String environmentFile)
	throws FileNotFoundException,
	    IOException
	{
		FileInputStream fis = null;
		try
		{
			File envFile = new File(environmentFile);
			Properties properties = new Properties();
			fis = new FileInputStream(envFile);
			properties.load(fis);
			return properties;
		}
		catch (FileNotFoundException e)
		{
//			log.error("could not find properties file: " + e.getLocalizedMessage());
			throw e;
		}
		catch (IOException e)
		{
//			log.error("error reading properties file: ", e);
			throw e;
		}
		finally
		{
			try
			{
				if (fis != null)
				{
					fis.close();
				}
			}
			catch (IOException e)
			{
				// too late to care at this point
			}
		}
	}

	private static <T> T newEnvironment(Class<T> type)
    {
		T instance = null;

	    try
        {
	        instance = type.newInstance();
        }
        catch (InstantiationException e)
        {
	        e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
	        e.printStackTrace();
        }

        return instance;
    }
}
