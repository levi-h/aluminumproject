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
 * Renames a file.
 * <p>
 * Note that the <em>rename file</em> action does not support moving files; the {@link MoveFile move file} action does.
 *
 * @author levi_h
 */
public class RenameFile extends AbstractAction {
	private File source;
	private String targetName;

	/**
	 * Creates a <em>rename file</em> action.
	 */
	public RenameFile() {}

	/**
	 * Sets the file that should be renamed.
	 *
	 * @param source the source file
	 */
	@ActionParameterInformation(required = true)
	public void setSource(File source) {
		this.source = source;
	}

	/**
	 * Sets the name that the file should get.
	 *
	 * @param targetName the new name for the file
	 */
	@ActionParameterInformation(required = true)
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public void execute(Context context, Writer writer) throws ActionException {
		File target = new File(source.getParentFile(), targetName);

		if (!source.getParentFile().equals(target.getParentFile())) {
			throw new ActionException("the rename file action does not support moving files");
		} else if (target.exists()) {
			throw new ActionException("can't rename '", source.getName(), "' to '", targetName, "': ",
				"a file with that name already exists");
		} else if (!source.renameTo(target)) {
			throw new ActionException("can't rename '", source.getName(), "' to '", targetName, "'");
		}
	}
}