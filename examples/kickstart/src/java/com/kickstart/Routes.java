package com.kickstart;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.kickstart.service.KickStartService;
import com.strategicgains.restexpress.route.RoutesDeclaration;

/**
 * @author toddf
 * @since May 21, 2010
 */
public class Routes
extends RoutesDeclaration
{
	private KickStartService service;
	
	public Routes()
	{
		super();
		this.service = new KickStartService();
	}
	
	@Override
	protected void defineRoutes()
	{
		// Maps /kickstart uri with optional format ('json' or 'xml'), accepting
		// POST HTTP method only.  Calls KickStartService.create(Request, Reply).
		uri("/kickstart.{format}", service)
			.method(HttpMethod.POST);

		// Maps /kickstart uri with required orderId and optional format identifier
		// to the KickStartService.  Accepts only GET, PUT, DELETE HTTP methods.
		// Names this route to allow returning links from read resources in
		// KickStartService methods via call to LinkUtils.asLinks().
		uri("/kickstart/{orderId}.{format}", service)
			.method(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
			.name("KickstartOrderUri");
	}
}
