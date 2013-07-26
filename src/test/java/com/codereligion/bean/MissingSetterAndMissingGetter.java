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
/**
 * 
 */
package com.codereligion.bean;

/**
 * Bean which provides three properties which are either readable, writeable or both.
 * 
 * @author Sebastian Gröbler
 * @since 23.06.2013
 */
public class MissingSetterAndMissingGetter {

	private String writeableAndReadableProperty;
	private boolean writeableProperty;
	private Integer readableProperty;

	public String getWriteableAndReadableProperty() {
		return writeableAndReadableProperty;
	}

	public void setWriteableAndReadableProperty(String foo) {
		this.writeableAndReadableProperty = foo;
	}

	public Integer getReadableProperty() {
		return readableProperty;
	}

	public void setWriteableProperty(boolean bar) {
		this.writeableProperty = bar;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (writeableProperty ? 1231 : 1237);
		result = prime * result + ((readableProperty == null) ? 0 : readableProperty.hashCode());
		result = prime * result + ((writeableAndReadableProperty == null) ? 0 : writeableAndReadableProperty.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MissingSetterAndMissingGetter other = (MissingSetterAndMissingGetter) obj;
		if (writeableProperty != other.writeableProperty)
			return false;
		if (readableProperty == null) {
			if (other.readableProperty != null)
				return false;
		} else if (!readableProperty.equals(other.readableProperty))
			return false;
		if (writeableAndReadableProperty == null) {
			if (other.writeableAndReadableProperty != null)
				return false;
		} else if (!writeableAndReadableProperty.equals(other.writeableAndReadableProperty))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MissingSetterAndMissingGetter [writeableAndReadableProperty=" + writeableAndReadableProperty + ", writeableProperty=" + writeableProperty + ", readableProperty="
				+ readableProperty + "]";
	}

}
