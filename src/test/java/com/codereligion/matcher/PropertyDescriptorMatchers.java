/**
 * Copyright 2013 www.codereligion.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codereligion.matcher;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;

import java.beans.PropertyDescriptor;
import org.hamcrest.Matcher;

/**
 * TODO
 * 
 * @author Sebastian Gröbler
 * @since 24.06.2013
 */
public class PropertyDescriptorMatchers {

	public static Matcher<Iterable<? super PropertyDescriptor>> containsProperty(final String propertyName) {
		return hasItem(hasProperty("name", equalTo(propertyName)));
	}
}
