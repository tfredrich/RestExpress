/*
    Copyright 2010, Strategic Gains, Inc.

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
package com.strategicgains.restx.marshaling;

import java.io.InputStreamReader;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author toddf
 * @since Jul 12, 2010
 */
public class JsonMarshaler
{
	private Gson gson = new GsonBuilder().create();

	public <T> T fromJson(ChannelBuffer jsonBuffer, Class<T> classOfT)
	{
		return gson.fromJson(new InputStreamReader(new ChannelBufferInputStream(jsonBuffer)), classOfT);
	}

	public <T> T fromJson(String json, Class<T> classOfT)
	{
		return gson.fromJson(json, classOfT);
	}
	
	public String toJson(Object object)
	{
		return gson.toJson(object);
	}
}
