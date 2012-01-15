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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@FunctionClass
@SuppressWarnings("javadoc")
public class Files {
	private Files() {}

	public static String name(@Named("file") File file) {
		return file.getName();
	}

	public static String extension(@Named("file") File file) {
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

	public static String nameWithoutExtension(@Named("file") File file) {
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

	public static long size(@Named("file") File file) {
		return file.length();
	}

	public static byte[] contents(@Named("file") File file) throws IOException {
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

	public static boolean isFile(@Named("file") File file) {
		return file.isFile();
	}

	public static boolean isDirectory(@Named("file") File file) {
		return file.isDirectory();
	}

	public static File newFile(@Named("directory") File directory, @Named("name") String name)
			throws IllegalArgumentException, IOException {
		File file = new File(directory, name);

		if (file.exists()) {
			throw new IllegalArgumentException(String.format("file '%s' already exists", file.getAbsolutePath()));
		}

		File parentFile = file.getParentFile();

		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}

		file.createNewFile();

		return file;
	}
}