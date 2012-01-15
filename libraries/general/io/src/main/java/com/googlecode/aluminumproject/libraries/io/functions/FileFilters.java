/*
 * Copyright 2009-2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.io.functions;

import com.googlecode.aluminumproject.annotations.FunctionClass;
import com.googlecode.aluminumproject.annotations.Named;

import java.io.File;
import java.io.FileFilter;

@FunctionClass
@SuppressWarnings("javadoc")
public class FileFilters {
	private FileFilters() {}

	public static FileFilter files() {
		return new FileFilter() {
			public boolean accept(File file) {
				return file.isFile();
			}
		};
	}

	public static FileFilter directories() {
		return new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		};
	}

	public static FileFilter filesNamedLike(final @Named("pattern") String pattern) {
		return new FileFilter() {
			public boolean accept(File file) {
				return file.getName().matches(pattern);
			}
		};
	}

	public static FileFilter filesWithExtension(final @Named("extension") String extension) {
		return new FileFilter() {
			public boolean accept(File file) {
				return Files.extension(file).equalsIgnoreCase(extension);
			}
		};
	}
}