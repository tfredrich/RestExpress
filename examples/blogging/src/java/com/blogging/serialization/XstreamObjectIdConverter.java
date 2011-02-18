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
package com.blogging.serialization;

import org.bson.types.ObjectId;

import com.thoughtworks.xstream.converters.SingleValueConverter;

/**
 * @author toddf
 * @since Feb 16, 2011
 */
public class XstreamObjectIdConverter
implements SingleValueConverter
{
	@SuppressWarnings("rawtypes")
    @Override
	public boolean canConvert(Class aClass)
	{
		return ObjectId.class.isAssignableFrom(aClass);
	}

	@Override
	public Object fromString(String value)
	{
		return new ObjectId(value);
	}

	@Override
	public String toString(Object objectId)
	{
		return ((ObjectId) objectId).toString();
	}
}
