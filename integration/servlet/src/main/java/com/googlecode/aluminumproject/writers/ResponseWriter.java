/*
 * Copyright 2009-2011 Levi Hoogenberg
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

import com.googlecode.aluminumproject.AluminumException;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * Writes to the output stream of a {@link HttpServletResponse servlet response}.
 *
 * @author levi_h
 */
public class ResponseWriter extends AbstractOutputStreamWriter {
	private HttpServletResponse response;

	/**
	 * Creates a response writer.
	 *
	 * @param response the response to write to
	 */
	public ResponseWriter(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	protected OutputStream createOutputStream() throws AluminumException {
		try {
			return response.getOutputStream();
		} catch (IOException exception) {
			throw new AluminumException(exception, "can't create response writer");
		}
	}

	@Override
	public void clear() throws AluminumException {
		checkOpen();

		response.resetBuffer();
	}

	@Override
	protected void closeOutputStream() {}
}