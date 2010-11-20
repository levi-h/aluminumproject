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
package com.googlecode.aluminumproject.utilities.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Provides utility methods related to resource finder tests.
 *
 * @author levi_h
 */
class ResourceFinderTestUtilities {
	private ResourceFinderTestUtilities() {}

	/**
	 * Reads the contents of a URL.
	 *
	 * @param url the URL to read
	 * @return the textual contents of the resource at the given location
	 * @throws IOException when the resource can't be read
	 */
	public static String read(URL url) throws IOException {
		InputStream in = url.openStream();

		try {
			OutputStream out = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int bytesRead;

			while ((bytesRead = in.read(buffer)) > 0) {
				out.write(buffer, 0, bytesRead);
			}

			return out.toString();
		} finally {
			in.close();
		}
	}
}