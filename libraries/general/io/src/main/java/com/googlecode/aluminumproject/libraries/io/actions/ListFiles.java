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
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.libraries.io.functions.FileFilters;
import com.googlecode.aluminumproject.writers.Writer;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

/**
 * Lists files and directories.
 *
 * @author levi_h
 */
public class ListFiles extends AbstractAction {
	private File location;

	private String resultName;
	private String resultScope;

	private FileFilter filter;

	/**
	 * Sets the location in which will be looked for files.
	 *
	 * @param location the location to use
	 */
	@ActionParameterInformation(name = "in", required = true)
	public void setLocation(File location) {
		this.location = location;
	}

	/**
	 * Sets the filter that should accept the files.
	 *
	 * @param filter the filter to apply
	 * @see FileFilters
	 */
	public void setFilter(FileFilter filter) {
		this.filter = filter;
	}

	/**
	 * Sets the name of the result variable.
	 *
	 * @param resultName the name of the variable in which the found files should be stored
	 */
	@ActionParameterInformation(required = true)
	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	/**
	 * Sets the scope of the result variable.
	 *
	 * @param resultScope the scope that should be used for the result variable
	 */
	public void setResultScope(String resultScope) {
		this.resultScope = resultScope;
	}

	public void execute(Context context, Writer writer) throws ActionException, ContextException {
		File[] files = location.listFiles(filter);

		if (files == null) {
			throw new ActionException("can't list files in ", location);
		} else {
			List<File> result = Arrays.asList(files);

			if (resultScope == null) {
				logger.debug("setting variable '", resultName, "' in innermost scope with result ", result);

				context.setVariable(resultName, result);
			} else {
				logger.debug("setting variable '", resultName, "' in scope '", resultScope, "' with result ", result);

				context.setVariable(resultScope, resultName, result);
			}
		}
	}
}