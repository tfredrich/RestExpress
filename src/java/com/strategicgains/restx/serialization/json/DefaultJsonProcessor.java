/*
 * Copyright 2010, Strategic Gains, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.strategicgains.restx.serialization.json;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.strategicgains.restx.serialization.SerializationProcessor;
import com.strategicgains.util.date.DateAdapterConstants;

/**
 * @author toddf
 * @since Mar 16, 2010
 */
public class DefaultJsonProcessor
implements SerializationProcessor
{
	private static final Gson GSON = new GsonBuilder()
		.disableHtmlEscaping()
		.registerTypeAdapter(Date.class, new GsonTimestampSerializer())
		.setDateFormat(DateAdapterConstants.TIMESTAMP_OUTPUT_FORMAT)
		.create();

    @Override
    public Object deserialize(String object, Type type)
    {
    	return GSON.fromJson((String) object, type);
    }

    @Override
    public String serialize(Object object)
    {
    	return GSON.toJson(object);
    }
}
