/*
 *  Copyright 2010, Pearson eCollege.
 */
package com.ecollege.storage.bigmap;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.strategicgains.restx.route.RouteMapping;

/**
 * @author toddf
 * @since May 21, 2010
 */
public class Routes
extends RouteMapping
{
	private BucketService bucketService;
	private StorageService storageService;

	@Override
	protected void initialize()
	{
		uri("/kickstart.{format}", new KickStartService())
			.method(HttpMethod.POST);

		uri("/kickstart/{orderId}.{format}", new KickStartService())
			.method(HttpMethod.GET)
			.method(HttpMethod.PUT)
			.method(HttpMethod.DELETE)
			.name("KickstartServiceUri");
	}
}
