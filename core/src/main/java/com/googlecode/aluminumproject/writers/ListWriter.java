/*
 * Copyright 2009-2010 Levi Hoogenberg
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
package com.googlecode.aluminumproject.writers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A writer that adds written objects to a {@link List list}.
 * <p>
 * The list that contains the written objects can be retrieved using the {@link #getList() getList method}. It only
 * contains objects that are {@link #flush() flushed}. It's possible to configure a list writer to flush automatically
 * by using the {@link #ListWriter(boolean) constructor that accepts a boolean}. In that case, the writer is flushed
 * after each {@link #write(Object) write operation}.
 *
 * @author levi_h
 */
public class ListWriter extends AbstractWriter {
	private List<Object> list;

	private int flushIndex;
	private boolean autoFlush;

	/**
	 * Creates a list writer that does not flush automatically.
	 */
	public ListWriter() {
		this(false);
	}

	/**
	 * Creates a list writer.
	 *
	 * @param autoFlush whether objects should be flushed as they're written or not
	 */
	public ListWriter(boolean autoFlush) {
		list = new ArrayList<Object>();

		flushIndex = 0;
		this.autoFlush = autoFlush;
	}

	/**
	 * Returns all written objects that have been flushed.
	 *
	 * @return a list with all flushed objects
	 */
	public List<?> getList() {
		return Collections.unmodifiableList(list.subList(0, flushIndex));
	}

	public void write(Object object) throws WriterException {
		checkOpen();

		list.add(object);

		if (autoFlush) {
			flush();
		}
	}

	@Override
	public void clear() throws WriterException {
		checkOpen();

		list.subList(flushIndex, list.size()).clear();
	}

	@Override
	public void flush() throws WriterException {
		checkOpen();

		flushIndex = list.size();
	}
}