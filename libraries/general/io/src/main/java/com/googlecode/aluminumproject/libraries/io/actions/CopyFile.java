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
package com.googlecode.aluminumproject.libraries.io.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Named;
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.writers.Writer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@SuppressWarnings("javadoc")
public class CopyFile extends AbstractAction {
	private @Required File source;
	private @Named("to") @Required File targetDirectory;

	public void execute(Context context, Writer writer) throws AluminumException {
		File target = new File(targetDirectory, source.getName());

		if (target.exists()) {
			throw new AluminumException("can't copy '", source, "' to ", targetDirectory.getAbsolutePath(), ": ",
				"the target directory already contains a file with that name");
		}

		FileInputStream in;
		FileOutputStream out;

		try {
			in = new FileInputStream(source);

			try {
				out = new FileOutputStream(target);

				try {
					byte[] buffer = new byte[8192];
					int bytesRead;

					while ((bytesRead = in.read(buffer)) > 0) {
						out.write(buffer, 0, bytesRead);
					}
				} finally {
					out.close();
				}
			} finally {
				in.close();
			}
		} catch (IOException exception) {
			throw new AluminumException(exception, "can't copy '", source, "' to ", targetDirectory);
		}
	}
}