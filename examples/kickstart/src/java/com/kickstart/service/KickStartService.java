package com.kickstart.service;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;

/**
 * @author toddf
 * @since Aug 31, 2010
 */
public class KickStartService
{
	public Object create(Request request, Response response)
	{
		response.setResponseCreated();
		return null;
	}

	public Object read(Request request, Response response)
	{
		return null;
	}

	public void update(Request request, Response response)
	{
		response.setResponseNoContent();
	}

	public void delete(Request request, Response response)
	{
		response.setResponseNoContent();
	}
}
