/*
    Copyright 2011, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package com.strategicgains.restexpress.domain.console;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author toddf
 * @since Jan 31, 2011
 */
public class RouteMetadata
{
	private String name;
	private String uri;
	private List<String> supportedFormats;
	private String defaultFormat;
	private boolean isSerialized;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUri()
	{
		return uri;
	}

	public void setUri(String uri)
	{
		this.uri = uri;
	}

	public List<String> getSupportedFormats()
	{
		return supportedFormats;
	}
	
	public void addSupportedFormat(String format)
	{
		if (getSupportedFormats() == null)
		{
			supportedFormats = new ArrayList<String>();
		}
		
		if (!getSupportedFormats().contains(format))
		{
			supportedFormats.add(format);
		}
	}

	public void addAllSupportedFormats(Collection<String> formats)
	{
		for (String format : formats)
		{
			addSupportedFormat(format);
		}
	}

	public String getDefaultFormat()
	{
		return defaultFormat;
	}

	public void setDefaultFormat(String defaultFormat)
	{
		this.defaultFormat = defaultFormat;
	}

	public boolean isSerialized()
	{
		return isSerialized;
	}

	public void setSerialized(boolean isSerialized)
	{
		this.isSerialized = isSerialized;
	}
}
