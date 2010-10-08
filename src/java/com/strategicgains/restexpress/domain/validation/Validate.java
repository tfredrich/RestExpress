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
package com.strategicgains.restexpress.domain.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotations for domain property validations.
 * 
 * @author toddf
 * @since Oct 7, 2010
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Validate
{
	String name() default "";
	boolean required() default false;
	int maxLength() default -1;
	int lessThan() default Integer.MIN_VALUE;
	int greaterThan() default Integer.MAX_VALUE;
}
