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
import com.googlecode.aluminumproject.writers.Writer;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

/**
 * Lists files and directories in a certain location, optionally matching a particular filter. The results will be
 * written (as a {@link List list}) to the action's writer.
 *
 * @author levi_h
 */
public class ListFiles extends AbstractAction {
	@ActionParameterInformation(name = "in", required = true)
	private File location;

	private FileFilter filter;

	/**
	 * Creates a <em>list files</em> action.
	 */
	public ListFiles() {}

	public void execute(Context context, Writer writer) throws ActionException, ContextException {
		File[] files = location.listFiles(filter);

		if (files == null) {
			throw new ActionException("can't list files in ", location);
		} else {
			writer.write(Arrays.asList(files));
		}
	}
}