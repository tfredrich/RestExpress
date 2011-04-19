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
package com.strategicgains.restexpress.domain;


/**
 * Instantiates a minimal XLink instance with id and href.
 * 
 * @author toddf
 * @since Apr 19, 2011
 */
public class DefaultXLinkFactory
implements XLinkFactory
{
	@Override
	public XLink create(String id, String href)
	{
		return new XLink(id, href);
	}
}
