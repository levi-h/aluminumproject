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
package com.googlecode.aluminumproject.libraries.io.actions;

import com.googlecode.aluminumproject.annotations.ActionParameterInformation;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.writers.FileWriter;
import com.googlecode.aluminumproject.writers.Writer;
import com.googlecode.aluminumproject.writers.WriterException;

import java.io.File;

/**
 * Invokes its body with a {@link FileWriter file writer}. If the writer that the action is invoked with is decorated,
 * then the file writer will inherit all decorative writers.
 *
 * @author levi_h
 */
public class WriteFile extends AbstractAction {
	private File target;

	private boolean append;

	/**
	 * Creates a <em>write file</em> action.
	 */
	public WriteFile() {}

	/**
	 * Sets the target file.
	 *
	 * @param target the file to write to
	 */
	@ActionParameterInformation(required = true)
	public void setTarget(File target) {
		this.target = target;
	}

	/**
	 * Sets whether output should be appended or not. By default, the file contents will be replaced.
	 *
	 * @param append {@code true} to append output to the file, {@code false} to replace it
	 */
	public void setAppend(boolean append) {
		this.append = append;
	}

	public void execute(Context context, Writer writer) throws ActionException, WriterException {
		logger.debug("invoking body with file writer (target: ", target, ", append: ", append, ")");

		FileWriter fileWriter = new FileWriter(target, append);

		try {
			invokeBody(context, writer, fileWriter);
		} finally {
			fileWriter.close();
		}
	}
}