/*
 * Copyright 2009-2012 Aluminum project
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
import com.googlecode.aluminumproject.annotations.UsableAsFunction;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.writers.Writer;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

@SuppressWarnings("javadoc")
@UsableAsFunction(resultType = "List<java.io.File>", argumentParameters = {"in", "filter"})
public class ListFiles extends AbstractAction {
	private @Named("in") @Required File location;

	private FileFilter filter;

	public void execute(Context context, Writer writer) throws AluminumException {
		File[] files = location.listFiles(filter);

		if (files == null) {
			throw new AluminumException("can't list files in ", location);
		} else {
			writer.write(Arrays.asList(files));
		}
	}
}