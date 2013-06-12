/*
 * Copyright 2012 www.codereligion.com
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
 * TODO
 * 
 * @author sgroebler
 * @since 12.06.2013
 */
public class HasReadMethod implements Predicate<PropertyDescriptor> {
	
	public static Predicate<PropertyDescriptor> INSTANCE = new HasReadMethod();

	@Override
	public boolean apply(@Nullable PropertyDescriptor input) {
		return input.getReadMethod() != null;
	}
}
