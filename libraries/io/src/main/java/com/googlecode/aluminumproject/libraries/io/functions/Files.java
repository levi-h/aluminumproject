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
package com.googlecode.aluminumproject.libraries.io.functions;

import com.googlecode.aluminumproject.annotations.FunctionClass;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Contains functions that provide information about files.
 *
 * @author levi_h
 */
@FunctionClass
public class Files {
	private Files() {}

	/**
	 * Returns the name of a file. This method delegates to the file's {@link File#getName() getName method}.
	 *
	 * @param file the file to determine the name of
	 * @return the name of the file
	 */
	public static String name(File file) {
		return file.getName();
	}

	/**
	 * Returns the extension of a file. If the file does not have an extension, the empty string is returned.
	 *
	 * @param file the file to determine the extension of
	 * @return the given file's extension (may be empty, but never {@code null})
	 */
	public static String extension(File file) {
		String extension;

		String name = file.getName();

		int positionOfLastDot = name.lastIndexOf('.');

		if (positionOfLastDot == -1) {
			extension = "";
		} else {
			extension = name.substring(positionOfLastDot + 1);
		}

		return extension;
	}

	/**
	 * Returns the name of a file without its extension.
	 *
	 * @param file the file to determine the extensionless name of
	 * @return the name of the given file, without its extension
	 */
	public static String nameWithoutExtension(File file) {
		String nameWithoutExtension;

		String name = file.getName();

		int positionOfLastDot = name.lastIndexOf('.');

		if (positionOfLastDot == -1) {
			nameWithoutExtension = name;
		} else {
			nameWithoutExtension = name.substring(0, positionOfLastDot);
		}

		return nameWithoutExtension;
	}

	/**
	 * Determines the size of a file in bytes.
	 *
	 * @param file the file to determine the size of
	 * @return the size of the given file
	 */
	public static long size(File file) {
		return file.length();
	}

	/**
	 * Reads the contents of a file.
	 *
	 * @param file the file to read
	 * @return the contents of the file
	 * @throws IOException when the contents of the file can't be determined
	 */
	public static byte[] contents(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		ByteArrayOutputStream out = new ByteArrayOutputStream((int) file.length());

		byte[] buffer = new byte[8192];
		int bytesRead;

		try {
			while ((bytesRead = in.read(buffer)) > 0) {
				out.write(buffer, 0, bytesRead);
			}
		} finally {
			in.close();
		}

		return out.toByteArray();
	}

	/**
	 * Determines whether a file is a normal file.
	 *
	 * @param file the file to check
	 * @return {@code true} if the given file is a normal file, {@code false} if it isn't (e.g. when it's a directory)
	 */
	public static boolean isFile(File file) {
		return file.isFile();
	}

	/**
	 * Determines whether a file is a directory.
	 *
	 * @param file the file to check
	 * @return {@code true} if the given file is a directory, {@code false} if it isn't
	 */
	public static boolean isDirectory(File file) {
		return file.isDirectory();
	}

	/**
	 * Creates a file and returns it.
	 *
	 * @param directory the directory in which to create the file
	 * @param name the name of the new file
	 * @return the new file
	 * @throws IllegalArgumentException when a file with the given name already exists in the specified directory
	 * @throws IOException when the file can't be created
	 */
	public static File newFile(File directory, String name) throws IllegalArgumentException, IOException {
		File file = new File(directory, name);

		if (file.exists()) {
			throw new IllegalArgumentException(String.format("file '%s' already exists", file.getAbsolutePath()));
		}

		file.createNewFile();

		return file;
	}
}