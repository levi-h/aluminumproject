/*
 * Copyright 2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.converters.ds;

import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.converters.Converter;
import com.googlecode.aluminumproject.converters.ConverterException;
import com.googlecode.aluminumproject.converters.ConverterRegistry;
import com.googlecode.aluminumproject.utilities.UtilityException;
import com.googlecode.aluminumproject.utilities.text.Splitter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Converts strings to {@link Map maps}. The expected format is {@code [a: 1, b: 2]}.
 *
 * @author levi_h
 */
public class StringToMapConverter implements Converter<String> {
	private @Injected Configuration configuration;

	/**
	 * Creates a string to map converter.
	 */
	public StringToMapConverter() {}

	public boolean supportsSourceType(Class<? extends String> sourceType) {
		return true;
	}

	public boolean supportsTargetType(Type targetType) {
		boolean targetTypeIsInterface = (targetType == Map.class);
		boolean targetTypeIsParameterisedInterface =
			((targetType instanceof ParameterizedType) && ((ParameterizedType) targetType).getRawType() == Map.class);

		return targetTypeIsInterface || targetTypeIsParameterisedInterface;
	}

	public Object convert(String value, Type targetType, Context context) throws ConverterException {
		if (!supportsTargetType(targetType)) {
			throw new ConverterException("expected map as target type, not ", targetType);
		}

		ConverterRegistry converterRegistry = configuration.getConverterRegistry();

		Map<Object, Object> map = new LinkedHashMap<Object, Object>();

		Type keyType;
		Type valueType;

		if (targetType == Map.class) {
			keyType = Object.class;
			valueType = Object.class;
		} else {
			Type[] typeArguments = ((ParameterizedType) targetType).getActualTypeArguments();

			keyType = typeArguments[0];
			valueType = typeArguments[1];
		}

		for (Map.Entry<String, String> entry: getEntries(value)) {
			map.put(converterRegistry.convert(entry.getKey(), keyType, context),
				converterRegistry.convert(entry.getValue(), valueType, context));
		}

		return map;
	}

	private List<Map.Entry<String, String>> getEntries(String map) throws ConverterException {
		EntryProcessor entryProcessor = new EntryProcessor();

		try {
			new Splitter(EntryProcessor.SEPARATOR_PATTERNS, '\\').split(map, entryProcessor);
		} catch (UtilityException exception) {
			throw new ConverterException(exception, "can't split map '", map, "'");
		}

		return entryProcessor.entries;
	}

	private static class EntryProcessor implements Splitter.TokenProcessor {
		private List<Map.Entry<String, String>> entries;

		private Entry entry;

		private Collection<String> expectedSeparatorPatterns;

		public EntryProcessor() {
			entries = new LinkedList<Map.Entry<String, String>>();

			expectedSeparatorPatterns = Collections.singleton(OPENING_BRACKET);
		}

		public void process(String token, String separator, String separatorPattern) throws UtilityException {
			if (expectedSeparatorPatterns.contains(separatorPattern)) {
				if (separatorPattern == null) {
					if (!token.equals("")) {
						throw new UtilityException("unexpected token after map: '", token, "'");
					}
				} else if (separatorPattern.equals(OPENING_BRACKET)) {
					if (token.equals("")) {
						expectedSeparatorPatterns = Arrays.asList(COLON, CLOSING_BRACKET);
					} else {
						throw new UtilityException("unexpected token before map: '", token, "'");
					}
				} else if (separatorPattern.equals(COLON)) {
					entry = new Entry(token);

					expectedSeparatorPatterns = Arrays.asList(COMMA, CLOSING_BRACKET);
				} else {
					if (entry != null) {
						entry.setValue(token);
						entries.add(entry);

						entry = null;
					}

					expectedSeparatorPatterns = Collections.singleton(separatorPattern.equals(COMMA) ? COLON : null);
				}
			} else {
				throw new UtilityException("unexpected separator: '", separator, "'");
			}
		}

		private static class Entry implements Map.Entry<String, String> {
			private String key;
			private String value;

			public Entry(String key) {
				this.key = key;
			}

			public String getKey() {
				return key;
			}

			public String getValue() {
				return value;
			}

			public String setValue(String value) {
				String previousValue = value;

				this.value = value;

				return previousValue;
			}

			@Override
			public int hashCode() {
				return key.hashCode() ^ value.hashCode();
			}

			@Override
			public boolean equals(Object object) {
				return (object instanceof Entry) && equals((Entry) object);
			}

			private boolean equals(Entry entry) {
				return key.equals(entry.key) && value.equals(entry.value);
			}
		}

		public final static String OPENING_BRACKET = "\\[";
		public final static String COLON = "\\s*:\\s*";
		public final static String COMMA = "\\s*,\\s*";
		public final static String CLOSING_BRACKET = "\\]";

		public final static List<String> SEPARATOR_PATTERNS =
			Arrays.asList(OPENING_BRACKET, COLON, COMMA, CLOSING_BRACKET);
	}
}