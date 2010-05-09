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

/**
 * An output to write to. It can delegate to an underlying writer or keep a buffer in memory. Although the {@link
 * #write(Object) write method} accepts any object, implementations may put a restriction on which objects can be
 * written.
 *
 * @author levi_h
 */
public interface Writer {
	/**
	 * Writes an object.
	 *
	 * @param object the object to write
	 * @throws WriterException when this writer is closed or when the object can't be written
	 */
	void write(Object object) throws WriterException;

	/**
	 * Removes all objects that have been {@link #write(Object) written} to this writer since the last time it was
	 * {@link #flush() flushed}. Not all writers support this operation.
	 *
	 * @throws WriterException when this writer is closed or can't be cleared
	 */
	void clear() throws WriterException;

	/**
	 * Flushes everything that has been {@link #write written} to this writer. If there's nothing to flush, nothing will
	 * happen. If this writer wraps another writer, the underlying writer will be flushed as well.
	 *
	 * @throws WriterException when this writer is closed or when something goes wrong while flushing
	 */
	void flush() throws WriterException;

	/**
	 * {@link #flush() Flushes} and closes this writer. If this writer wraps another writer, that writer will be closed
	 * as well.
	 * <p>
	 * After a writer has been closed, it can no longer be used.
	 *
	 * @throws WriterException when this writer can't be closed or has already been closed
	 */
	void close() throws WriterException;
}