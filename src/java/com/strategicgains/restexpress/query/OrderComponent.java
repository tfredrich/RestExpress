/*
 * Copyright 2011, Strategic Gains, Inc.
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
package com.strategicgains.restexpress.query;

/**
 * @author toddf
 * @since May 25, 2011
 */
public class OrderComponent
{
	private boolean isDescending;
	private String fieldName;
	
	public OrderComponent(String fieldName, boolean isDescending)
	{
		super();
		this.isDescending = isDescending;
		this.fieldName = fieldName;
	}
	
	public boolean isAscending()
	{
		return !isDescending;
	}
	
	public boolean isDescending()
	{
		return isDescending;
	}
	
	public String getFieldName()
	{
		return fieldName;
	}
}
