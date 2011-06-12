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
package com.googlecode.aluminumproject.libraries.io.functions;

import com.googlecode.aluminumproject.annotations.FunctionClass;
import com.googlecode.aluminumproject.annotations.Named;

import java.io.File;
import java.io.FileFilter;

/**
 * Contains functions that create {@link FileFilter file filters}.
 *
 * @author levi_h
 */
@FunctionClass
public class FileFilters {
	private FileFilters() {}

	/**
	 * Creates a filter that selects normal files.
	 *
	 * @return the new file filter
	 */
	public static FileFilter files() {
		return new FileFilter() {
			public boolean accept(File file) {
				return file.isFile();
			}
		};
	}

	/**
	 * Creates a filter that selects directories.
	 *
	 * @return the new file filter
	 */
	public static FileFilter directories() {
		return new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		};
	}

	/**
	 * Creates a filter that selects files that have names that match a certain pattern (a regular expression).
	 *
	 * @param pattern the regular expression that the files' names should match
	 * @return the new file filter
	 */
	public static FileFilter filesNamedLike(final @Named("pattern") String pattern) {
		return new FileFilter() {
			public boolean accept(File file) {
				return file.getName().matches(pattern);
			}
		};
	}

	/**
	 * Creates a filter that selects files with a certain extension.
	 *
	 * @param extension the extension that the files should have
	 * @return the new file filter
	 */
	public static FileFilter filesWithExtension(final @Named("extension") String extension) {
		return new FileFilter() {
			public boolean accept(File file) {
				return Files.extension(file).equalsIgnoreCase(extension);
			}
		};
	}
}