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
import com.googlecode.aluminumproject.writers.Writer;

import java.io.File;

/**
 * Moves a file to another directory.
 *
 * @author levi_h
 */
public class MoveFile extends AbstractAction {
	@ActionParameterInformation(required = true)
	private File source;

	@ActionParameterInformation(name = "to", required = true)
	private File targetDirectory;

	/**
	 * Creates a <em>move file</em> action.
	 */
	public MoveFile() {}

	public void execute(Context context, Writer writer) throws ActionException {
		File target = new File(targetDirectory, source.getName());

		if (target.exists()) {
			throw new ActionException(targetDirectory, " already contains a file named '", source.getName(), "'");
		} else if (!source.renameTo(target)) {
			throw new ActionException("can't move '", source.getName(), "' to ", targetDirectory);
		}
	}
}