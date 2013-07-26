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
package com.codereligion.reflect.internal;

import com.google.common.base.Predicate;
import java.beans.PropertyDescriptor;
import javax.annotation.Nullable;

/**
 * Applies true to {@link PropertyDescriptor}s when they provide a read method.
 * 
 * @author Sebastian Gröbler
 * @author Willi Schönborn
 * @since 12.06.2013
 */
public enum HasReadMethod implements Predicate<PropertyDescriptor> {

	INSTANCE;

	@Override
	public boolean apply(@Nullable PropertyDescriptor input) {
		return input != null && input.getReadMethod() != null;
	}
}
