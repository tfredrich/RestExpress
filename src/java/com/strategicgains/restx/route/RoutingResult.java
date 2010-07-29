package com.strategicgains.restx.route;

/**
 * @author toddf
 *
 */
public class RoutingResult
{
	private Route route;
	private Object object;
	
	public RoutingResult(Route route, Object object)
	{
		super();
		this.route = route;
		this.object = object;
	}

	public Route getRoute()
	{
		return route;
	}

	public Object getObject()
	{
		return object;
	}
	
	public boolean shouldSerializeResponse()
	{
		return getRoute().shouldSerializeResponse();
	}
}
