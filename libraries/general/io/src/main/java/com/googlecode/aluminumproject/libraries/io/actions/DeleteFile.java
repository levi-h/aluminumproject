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

import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.writers.Writer;

import java.io.File;

/**
 * Deletes a file.
 * <p>
 * If the file is a directory, all of its contents will be deleted as well.
 *
 * @author levi_h
 */
public class DeleteFile extends AbstractAction {
	private @Required File file;

	/**
	 * Creates a <em>delete file</em> action.
	 */
	public DeleteFile() {}

	public void execute(Context context, Writer writer) throws ActionException {
		delete(file);
	}

	private void delete(File file) throws ActionException {
		if (file.isDirectory()) {
			for (File fileInDirectory: file.listFiles()) {
				delete(fileInDirectory);
			}
		}

		if (!file.delete()) {
			throw new ActionException("can't delete file '%s'", file.getAbsolutePath());
		}
	}
}