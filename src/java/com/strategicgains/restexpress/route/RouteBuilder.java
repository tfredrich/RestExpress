package com.strategicgains.restexpress.route;

import static org.jboss.netty.handler.codec.http.HttpMethod.DELETE;
import static org.jboss.netty.handler.codec.http.HttpMethod.GET;
import static org.jboss.netty.handler.codec.http.HttpMethod.HEAD;
import static org.jboss.netty.handler.codec.http.HttpMethod.OPTIONS;
import static org.jboss.netty.handler.codec.http.HttpMethod.POST;
import static org.jboss.netty.handler.codec.http.HttpMethod.PUT;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.domain.console.RouteMetadata;
import com.strategicgains.restexpress.domain.console.UriMetadata;
import com.strategicgains.restexpress.exception.ConfigurationException;

/**
 * Builds a route for a single URI.  If a URI is given with no methods or actions, the builder
 * creates routes for the GET, POST, PUT, and DELETE HTTP methods for the given URI.
 * 
 * @author toddf
 * @since 2010
 */
public abstract class RouteBuilder
{
	// SECTION: CONSTANTS

	static final String DELETE_ACTION_NAME = "delete";
	static final String GET_ACTION_NAME = "read";
	static final String POST_ACTION_NAME = "create";
	static final String PUT_ACTION_NAME = "update";
	static final String HEAD_ACTION_NAME = "headers";
	static final String OPTION_ACTION_NAME = "options";
	static final List<HttpMethod> DEFAULT_HTTP_METHODS = Arrays.asList(new HttpMethod[] {GET, POST, PUT, DELETE});
	static final Map<HttpMethod, String> ACTION_MAPPING = new HashMap<HttpMethod, String>();

	static
	{
		ACTION_MAPPING.put(DELETE, DELETE_ACTION_NAME);
		ACTION_MAPPING.put(GET, GET_ACTION_NAME);
		ACTION_MAPPING.put(POST, POST_ACTION_NAME);
		ACTION_MAPPING.put(PUT, PUT_ACTION_NAME);
		ACTION_MAPPING.put(HEAD, HEAD_ACTION_NAME);
		ACTION_MAPPING.put(OPTIONS, OPTION_ACTION_NAME);
	}

	
	// SECTION: INSTANCE VARIABLES

	private String uri;
	private List<HttpMethod> methods = new ArrayList<HttpMethod>();
	private List<String> supportedFormats = new ArrayList<String>();
	private String defaultFormat = null;
	private Map<HttpMethod, String> actionNames = new HashMap<HttpMethod, String>();
	private Object controller;
	private boolean shouldSerializeResponse = true;
	private String name;
	
	/**
	 * Create a RouteBuilder instance for the given URI pattern. URIs that match the pattern
	 * will map to methods on the POJO controller.
	 * 
	 * @param uri a URI pattern
	 * @param controller the POJO service controller.
	 */
	public RouteBuilder(String uri, Object controller)
	{
		super();
		this.uri = uri;
		this.controller = controller;
	}
	
	/**
	 * Map a service method name (action) to a particular HTTP method (e.g. GET, POST, PUT, DELETE, HEAD, OPTIONS)
	 * 
	 * @param action the name of a method within the service POJO.
	 * @param method the HTTP method that should invoke the service method.
	 * @return the RouteBuilder instance.
	 */
	public RouteBuilder action(String action, HttpMethod method)
	{
		if (!actionNames.containsKey(method))
		{
			actionNames.put(method, action);
		}

		if (!methods.contains(method))
		{
			methods.add(method);
		}

		return this;
	}
	
	/**
	 * Defines HTTP methods that the route will support (e.g. GET, PUT, POST, DELETE, OPTIONS, HEAD).
	 * This utilizes the default HTTP method to service action mapping (e.g. GET maps to read(), PUT to update(), etc.).
	 * 
	 * @param methods the HTTP methods supported by the route.
	 * @return the RouteBuilder instance.
	 */
	public RouteBuilder method(HttpMethod... methods)
	{
		for (HttpMethod method : methods)
		{
			if (!this.methods.contains(method))
			{
				this.methods.add(method);
			}
		}

		return this;
	}

	/**
	 * Turns off serialization of the response--returns the response body as pain text.
	 * 
	 * @return the RouteBuilder instance.
	 */
	public RouteBuilder noSerialization()
	{
		this.shouldSerializeResponse = false;
		return this;
	}

	/**
	 * Turns on response serialization (the default) so the response body will be serialized
	 * (e.g. into JSON or XML).
	 * 
	 * @return the RouteBuilder instance.
	 */
	public RouteBuilder performSerialization()
	{
		this.shouldSerializeResponse = true;
		return this;
	}
	
	/**
	 * Give the route a known name to facilitate retrieving the route by name.  This facilitates
	 * using the route URI pattern to create Link instances via LinkUtils.asLinks().
	 * 
	 * The name must be unique for each URI pattern.
	 * 
	 * @param name the given name of the route for later retrieval.
	 * @return the RouteBuilder instance.
	 */
	public RouteBuilder name(String name)
	{
		this.name = name;
		return this;
	}
	
	public RouteBuilder format(String format)
	{
		if (!supportedFormats.contains(format))
		{
			supportedFormats.add(format);
		}
		
		return this;
	}
	
	public RouteBuilder defaultFormat(String format)
	{
		this.defaultFormat = format;
		return this;
	}
	
	
	// SECTION - BUILDER

	/**
	 * Build the Route instances.  The last step in the Builder process.
	 * 
	 * @return a List of Route instances.
	 */
	public List<Route> build()
	{
		if (methods.isEmpty())
		{
			methods = DEFAULT_HTTP_METHODS;
		}

		List<Route> routes = new ArrayList<Route>();
		String pattern = uri;

		if (pattern != null && !pattern.startsWith("/"))
		{
			pattern = "/" + pattern;
		}
		
		for (HttpMethod method : methods)
		{
			String actionName = actionNames.get(method);

			if (actionName == null)
			{
				actionName = ACTION_MAPPING.get(method);
			}
			
			Method action = determineActionMethod(controller, actionName);
			routes.add(newRoute(pattern, controller, action, method, shouldSerializeResponse, name, supportedFormats, defaultFormat));
		}
		
		return routes;
	}
	
	
	// SECTION: CONSOLE
	
	public RouteMetadata asMetadata()
	{
		RouteMetadata metadata = new RouteMetadata();
		metadata.setName(name);
		metadata.setSerialized(shouldSerializeResponse);
		metadata.setDefaultFormat(defaultFormat);
		metadata.addAllSupportedFormats(supportedFormats);
		
		for (HttpMethod method : methods)
		{
			metadata.addMethod(method.getName());
		}
		
		UriMetadata uriMeta = new UriMetadata(uri);
		List<Route> routes = build();

		for (Route route : routes)
		{
			uriMeta.addAllParameters(route.getUrlParameters());
		}

		metadata.setUri(uriMeta);
		return metadata;
	}

	
	// SECTION: UTILITY - SUBSCLASSES

	/**
     * @param pattern
     * @param controller
     * @param action
     * @param method
     * @param shouldSerializeResponse
     * @param name
	 * @param supportedFormats 
	 * @param defaultFormat
     * @return
     */
    protected abstract Route newRoute(String pattern, Object controller, Method action,
    	HttpMethod method, boolean shouldSerializeResponse, String name, List<String> supportedFormats, String defaultFormat);


	// SECTION: UTILITY - PRIVATE

	/**
	 * Attempts to find the actionName on the controller, assuming a signature of actionName(Request, Response), 
	 * and returns the action as a Method to be used later when the route is invoked.
	 * 
	 * @param controller a pojo that implements a method named by the action, with Request and Response as parameters.
	 * @param actionName the name of a method on the given controller pojo.
	 * @return a Method instance referring to the action on the controller.
	 * @throws ConfigurationException if an error occurs.
	 */
	private Method determineActionMethod(Object controller, String actionName)
	{
		try
		{
			return controller.getClass().getMethod(actionName, Request.class, Response.class);
		}
		catch (Exception e)
		{
			throw new ConfigurationException(e);
		}
	}
}
