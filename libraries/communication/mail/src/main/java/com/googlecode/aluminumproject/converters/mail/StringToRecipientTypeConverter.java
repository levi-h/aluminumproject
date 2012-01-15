/*
 * Copyright 2011-2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.converters.mail;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.converters.ClassBasedConverter;

import java.util.HashMap;
import java.util.Map;

import javax.mail.Message.RecipientType;

/**
 * Converts strings to {@link RecipientType recipient types} to, carbon copy (cc), blind carbon copy (bcc), and
 * newsgroups.
 */
public class StringToRecipientTypeConverter extends ClassBasedConverter<String, RecipientType> {
	private Map<String, RecipientType> recipientTypes;

	/**
	 * Creates a string to recipient type converter.
	 */
	public StringToRecipientTypeConverter() {
		recipientTypes = new HashMap<String, RecipientType>();
		recipientTypes.put("to", RecipientType.TO);
		recipientTypes.put("cc", RecipientType.CC);
		recipientTypes.put("bcc", RecipientType.BCC);
		recipientTypes.put("newsgroups", javax.mail.internet.MimeMessage.RecipientType.NEWSGROUPS);
	}

	@Override
	protected RecipientType convert(String value) throws AluminumException {
		RecipientType recipientType = recipientTypes.get(value.toLowerCase());

		if (recipientType == null) {
			throw new AluminumException("unknown recipient type: '", value, "'");
		} else {
			return recipientType;
		}
	}
}