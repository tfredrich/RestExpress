/*
 *   Copyright 2010, Pearson eCollege
 */
package com.strategicgains.restx.domain;

/**
 * An immutable object representing an ID with an xlink property.
 * 
 * @author toddf
 * @since Jul 23, 2010
 */
public class Link
{
	private String id;
	private String xlink;

	public Link(String id, String xlink)
    {
	    super();
	    this.id = id;
	    this.xlink = xlink;
    }

	public String getId()
    {
    	return id;
    }

	public String getXlink()
    {
    	return xlink;
    }
}
