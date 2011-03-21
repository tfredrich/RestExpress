/*
 * Copyright 2009, Strategic Gains, Inc.
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
package com.strategicgains.restexpress.util;

import org.jboss.netty.logging.InternalLogLevel;

/**
 * public facing enum for logging levels, mapped to Netty logger levels
 * 
 * @author kevwil
 * @since Feb 23, 2011
 */
public enum LogLevel
{
	/**
	 * 'DEBUG' log level
	 */
	DEBUG,
	/**
	 * 'INFO' log level
	 */
	INFO,
	/**
	 * 'WARN' log level
	 */
	WARN,
	/**
	 * 'ERROR' log level
	 */
	ERROR;
	
	/**
	 * translate this enum value into the enum value needed by Netty
	 * @return the corresponding org.jboss.netty.logging.InternalLogLevel
	 */
	public InternalLogLevel getNettyLogLevel()
	{
		return InternalLogLevel.valueOf(name());
	}
}
