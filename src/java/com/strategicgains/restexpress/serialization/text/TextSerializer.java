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
package com.strategicgains.restexpress.serialization.text;

import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.serialization.Serializer;

/**
 * @author toddf
 * @since Dec 22, 2010
 */
public abstract class TextSerializer
implements Serializer
{
	@Override
	public String getResultingContentType()
	{
		return RestExpress.CONTENT_TYPE_TEXT_PLAIN;
	}
}
