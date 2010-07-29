package com.strategicgains.restx.marshaling;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;

import com.thoughtworks.xstream.XStream;

/**
 * @author toddf
 * @since Jul 12, 2010
 */
public class XmlMarshaler
{
	private XStream xstream;
	
	public XmlMarshaler()
	{
		super();
		xstream = new XStream();
		
		// TODO: Add the ability to do this in sub-projects.
//		xstream.aliasType("bucket", Bucket.class);
//		xstream.alias("owner", Owner.class);
//		xstream.alias("link", Link.class);
	}

	public Object fromXml(ChannelBuffer xml)
	{
		return xstream.fromXML(new ChannelBufferInputStream(xml));
	}
	
	public String toXml(Object object)
	{
		return xstream.toXML(object);
	}
}
