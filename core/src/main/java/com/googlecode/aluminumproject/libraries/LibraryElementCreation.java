/*
 * Copyright 2012 Aluminum project
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
package com.googlecode.aluminumproject.libraries;

/**
 * The product of a {@link LibraryElement library element}.
 *
 * @param <T> the type of the factory that produces the library element creations
 */
public interface LibraryElementCreation<T extends LibraryElement> {
	/**
	 * Returns this library element creation's factory.
	 *
	 * @return the factory that produced this library element creation
	 */
	T getFactory();

	/**
	 * Sets the factory of this library element creation.
	 *
	 * @param factory the factory that produced this library element creation
	 */
	void setFactory(T factory);
}