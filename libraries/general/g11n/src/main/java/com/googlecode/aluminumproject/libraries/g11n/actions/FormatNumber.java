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
import com.googlecode.aluminumproject.libraries.g11n.model.NumberFormatType;
import com.googlecode.aluminumproject.writers.Writer;
import com.googlecode.aluminumproject.writers.WriterException;

import java.text.NumberFormat;

/**
 * Formats a number in a certain {@link NumberFormatType way} and writes the result away.
 *
 * @author levi_h
 */
public class FormatNumber extends AbstractAction {
	private Number value;

	private NumberFormatType type;

	private Boolean grouping;
	private Integer minimumIntegerDigits;
	private Integer maximumIntegerDigits;
	private Integer minimumFractionDigits;
	private Integer maximumFractionDigits;

	/**
	 * Creates a <em>format number</em> action.
	 */
	public FormatNumber() {
		type = NumberFormatType.CUSTOM;
	}

	/**
	 * Sets the number that should be formatted.
	 *
	 * @param value the number to format
	 */
	@ActionParameterInformation(required = true)
	public void setValue(Number value) {
		this.value = value;
	}

	/**
	 * Sets whether grouping will be used.
	 *
	 * @param grouping {@code true} to use grouping, {@code false} otherwise
	 */
	public void setGrouping(Boolean grouping) {
		this.grouping = grouping;
	}

	/**
	 * Sets how the number should be formatted. If no number format type is set, this action falls back to the custom
	 * type.
	 *
	 * @param type the way in which the number should be formatted
	 */
	public void setType(NumberFormatType type) {
		this.type = type;
	}

	/**
	 * Sets the minimum number of digits that will be used for the fractional part of the number.
	 *
	 * @param minimumFractionDigits the minimum number of fraction digits to use
	 */
	public void setMinimumFractionDigits(Integer minimumFractionDigits) {
		this.minimumFractionDigits = minimumFractionDigits;
	}

	/**
	 * Sets the maximum number of digits that will be used for the fractional part of the number.
	 *
	 * @param maximumFractionDigits the maximum number of fraction digits to use
	 */
	public void setMaximumFractionDigits(Integer maximumFractionDigits) {
		this.maximumFractionDigits = maximumFractionDigits;
	}

	/**
	 * Sets the minimum number of digits that will be used for the integer part of the number.
	 *
	 * @param minimumIntegerDigits the minimum number of integer digits to use
	 */
	public void setMinimumIntegerDigits(Integer minimumIntegerDigits) {
		this.minimumIntegerDigits = minimumIntegerDigits;
	}

	/**
	 * Sets the maximum number of digits that will be used for the integer part of the number.
	 *
	 * @param maximumIntegerDigits the maximum number of integer digits to use
	 */
	public void setMaximumIntegerDigits(Integer maximumIntegerDigits) {
		this.maximumIntegerDigits = maximumIntegerDigits;
	}

	public void execute(Context context, Writer writer) throws ContextException, WriterException {
		NumberFormat numberFormat = GlobalisationContext.from(context).getNumberFormatProvider().provide(type, context);

		if (grouping != null) {
			numberFormat.setGroupingUsed(grouping);
		}

		if (maximumFractionDigits != null) {
			numberFormat.setMaximumFractionDigits(maximumFractionDigits);
		}

		if (minimumFractionDigits != null) {
			numberFormat.setMinimumFractionDigits(minimumFractionDigits);
		}

		if (maximumIntegerDigits != null) {
			numberFormat.setMaximumIntegerDigits(maximumIntegerDigits);
		}

		if (minimumIntegerDigits != null) {
			numberFormat.setMinimumIntegerDigits(minimumIntegerDigits);
		}

		logger.debug("formatting number ", value, " using number format ", numberFormat);

		writer.write(numberFormat.format(value));
	}
}