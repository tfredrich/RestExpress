package com.strategicgains.kickstart;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.strategicgains.kickstart.service.KickStartService;
import com.strategicgains.restx.route.RouteMapping;

/**
 * @author toddf
 * @since May 21, 2010
 */
public class Routes
extends RouteMapping
{
	private KickStartService service;
	
	@Override
	public RouteMapping initialize()
	{
		service = new KickStartService();
		return super.initialize();
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

		// Maps /kickstart/resource uri with required 'someId' and format identifier
		// to the KickStartService.  Accepts, by default GET, PUT, POST, DELETE
		// HTTP methods.  Note that this resource URI would call the same 
		// create(), read(), update(), delete() methods in KickStartService
		// as the routes above, which is probably undesirable.
		// So we would probably want to map this resource to a different service
		// or map the HTTP methods to different service methods using the action()
		// DSL modifier.
		uri("/kickstart/resource/{someId}.{format}", service)
			.action("readResource", HttpMethod.GET)
			.action("createResource", HttpMethod.POST)
			.action("updateResource", HttpMethod.PUT)
			.action("deleteResource", HttpMethod.DELETE);
	}
}
