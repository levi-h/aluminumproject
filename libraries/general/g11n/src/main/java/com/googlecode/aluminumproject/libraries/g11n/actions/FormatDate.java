/*
 * Copyright 2010 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.g11n.actions;

import com.googlecode.aluminumproject.annotations.ActionParameterInformation;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.context.g11n.GlobalisationContext;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.g11n.model.DateFormatType;
import com.googlecode.aluminumproject.writers.Writer;
import com.googlecode.aluminumproject.writers.WriterException;

import java.text.DateFormat;
import java.util.Date;

/**
 * Formats a date in a certain {@link DateFormatType way} and writes the result to the writer.
 *
 * @author levi_h
 */
public class FormatDate extends AbstractAction {
	private Date value;

	private DateFormatType type;

	/**
	 * Creates a <em>format date</em> action.
	 */
	public FormatDate() {
		type = DateFormatType.CUSTOM;
	}

	/**
	 * Sets the date that should be formatted.
	 *
	 * @param value the date to format
	 */
	@ActionParameterInformation(required = true)
	public void setValue(Date value) {
		this.value = value;
	}

	/**
	 * Sets how the date should be formatted. By default, a custom date format will be used.
	 *
	 * @param type the type of the date format to use
	 */
	public void setType(DateFormatType type) {
		this.type = type;
	}

	public void execute(Context context, Writer writer) throws ContextException, WriterException {
		DateFormat dateFormat = GlobalisationContext.from(context).getDateFormatProvider().provide(type, context);

		logger.debug("formatting date ", value, " using date format ", dateFormat);

		writer.write(dateFormat.format(value));
	}
}