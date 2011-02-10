package com.kickstart.service;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.exception.NotFoundException;

/**
 * @author toddf
 * @since Aug 31, 2010
 */
public class KickStartController
{
	public Object create(Request request, Response response)
	{
		response.setResponseCreated();
		return null;
	}

	public Object read(Request request, Response response)
	{
		String id = request.getHeader("orderId");
		throw new NotFoundException("The order ID you requested was not found: " + id);
	}

	public void update(Request request, Response response)
	{
//		response.setResponseNoContent();
	}

	public void delete(Request request, Response response)
	{
//		response.setResponseNoContent();
	}
}
