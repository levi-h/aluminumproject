/*
 * Copyright 2010-2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.text.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.annotations.Named;
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.converters.ConverterRegistry;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.AbstractDynamicallyParameterisableAction;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.utilities.text.Splitter;
import com.googlecode.aluminumproject.utilities.text.Splitter.TokenProcessor;
import com.googlecode.aluminumproject.writers.NullWriter;
import com.googlecode.aluminumproject.writers.Writer;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Writes a formatted text.
 * <p>
 * The <em>format</em> action supports three format types: interpolation (the default), message format, and
 * printf-style. Parameters can be added by nesting {@link FormatParameter parameter actions}, and (for the
 * interpolation format) by providing dynamic parameters.
 *
 * @author levi_h
 */
public class Format extends AbstractDynamicallyParameterisableAction {
	private FormatType type;

	private @Required String formatString;
	private @Ignored List<Parameter> parameters;

	private @Injected Configuration configuration;

	/**
	 * Creates a <em>format</em> action.
	 */
	public Format() {
		type = FormatType.INTERPOLATION;

		parameters = new LinkedList<Parameter>();
	}

	/**
	 * Adds a format parameter.
	 *
	 * @param name the name of the parameter to add (may be {@code null})
	 * @param value the value of the parameter to add
	 */
	protected void addParameter(String name, Object value) {
		parameters.add(new Parameter(name, value));
	}

	public void execute(Context context, Writer writer) throws AluminumException {
		for (Map.Entry<String, ActionParameter> dynamicParameter: getDynamicParameters().entrySet()) {
			addParameter(dynamicParameter.getKey(), dynamicParameter.getValue().getValue(String.class, context));
		}

		getBody().invoke(context, new NullWriter());

		writer.write(type.format(formatString, parameters, configuration.getConverterRegistry()));
	}

	/**
	 * A possible format type.
	 *
	 * @author levi_h
	 */
	public static enum FormatType {
		/**
		 * Uses named parameters to format strings such as <code>Hello, {name}!</code>
		 */
		INTERPOLATION {
			@Override
			String format(String formatString, List<Parameter> parameters, ConverterRegistry converterRegistry)
					throws AluminumException {
				Interpolator interpolator = new Interpolator(getNamedParameters(parameters, converterRegistry));

				new Splitter(Arrays.asList("\\{", "\\}"), '\\').split(formatString, interpolator);

				return interpolator.getText();
			}

			class Interpolator implements TokenProcessor {
				private Map<String, String> namedParameters;

				private StringBuilder textBuilder;

				private boolean interpolating;

				public Interpolator(Map<String, String> namedParameters) {
					this.namedParameters = namedParameters;

					textBuilder = new StringBuilder();
				}

				public void process(String token, String separator, String separatorPattern) throws AluminumException {
					if (interpolating) {
						if (separator.equals("}")) {
							if (namedParameters.containsKey(token)) {
								textBuilder.append(namedParameters.get(token));
							} else {
								throw new AluminumException("unknown parameter: '", token, "'");
							}

							interpolating = false;
						} else {
							throw new AluminumException("unclosed opening brace encountered");
						}
					} else {
						if (separator.equals("{")) {
							interpolating = true;
						} else if (separator.equals("}")) {
							throw new AluminumException("unopened closing brace encountered");
						}

						textBuilder.append(token);
					}
				}

				public String getText() {
					return textBuilder.toString();
				}
			}
		},

		/**
		 * Uses positional parameters to format strings like <code>Hello, {0}!</code>.
		 *
		 * @see MessageFormat
		 */
		MESSAGE_FORMAT {
			@Override
			public String format(String formatString, List<Parameter> parameters, ConverterRegistry converterRegistry)
					throws AluminumException {
				try {
					return MessageFormat.format(formatString, getPositionalParameters(parameters).toArray());
				} catch (IllegalArgumentException exception) {
					throw new AluminumException(exception, "can't format text");
				}
			}
		},

		/**
		 * Uses positional parameters to format strings such as <code>Hello, %s!</code>
		 *
		 * @see Formatter
		 */
		PRINTF {
			@Override
			public String format(String formatString, List<Parameter> parameters, ConverterRegistry converterRegistry)
					throws AluminumException {
				try {
					return String.format(formatString, getPositionalParameters(parameters).toArray());
				} catch (IllegalFormatException exception) {
					throw new AluminumException(exception, "can't format text");
				}
			}
		};

		private static Map<String, String> getNamedParameters(
				List<Parameter> parameters, ConverterRegistry converterRegistry) throws AluminumException {
			Map<String, String> namedParameters = new HashMap<String, String>();

			for (Parameter parameter: parameters) {
				String name = parameter.name;

				if (name == null) {
					throw new AluminumException("unnamed parameter: ", parameter.value);
				} else if (namedParameters.containsKey(name)) {
					throw new AluminumException("duplicate parameter: '", name, "'");
				} else {
					namedParameters.put(name, (String) converterRegistry.convert(parameter.value, String.class));
				}
			}

			return namedParameters;
		}

		private static List<Object> getPositionalParameters(List<Parameter> parameters) throws AluminumException {
			List<Object> positionalParameters = new ArrayList<Object>();

			for (Parameter parameter: parameters) {
				if (parameter.name == null) {
					positionalParameters.add(parameter.value);
				} else {
					throw new AluminumException("named parameter: '", parameter.name, "'");
				}
			}

			return positionalParameters;
		}

		/**
		 * Formats a string, given a number of parameters.
		 *
		 * @param formatString the string to format
		 * @param parameters the parameters to use
		 * @param converterRegistry the converter registry to use
		 * @return the formatted string
		 * @throws AluminumException when the string can't be formatted
		 */
		abstract String format(String formatString, List<Parameter> parameters, ConverterRegistry converterRegistry)
			throws AluminumException;
	}

	private static class Parameter {
		private String name;
		private Object value;

		public Parameter(String name, Object value) {
			this.name = name;
			this.value = value;
		}
	}

	/**
	 * Adds an optionally named parameter to a <em>format</em> action.
	 *
	 * @author levi_h
	 */
	@Named("parameter")
	public static class FormatParameter extends AbstractAction {
		private String name;

		private @Required Object value;

		/**
		 * Creates a <em>format parameter</em> action.
		 */
		public FormatParameter() {}

		public void execute(Context context, Writer writer) throws AluminumException {
			findAncestorOfType(Format.class).addParameter(name, value);
		}
	}
}