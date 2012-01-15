/*
 * Copyright 2011-2012 Aluminum project
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
import com.googlecode.aluminumproject.converters.Converter;

import javax.mail.Message.RecipientType;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-mail", "fast"})
public class StringToRecipientTypeConverterTest {
	private Converter<String> converter;

	@BeforeMethod
	public void createConverter() {
		converter = new StringToRecipientTypeConverter();
	}

	public void toTypeShouldBeConvertible() {
		assert converter.convert("to", RecipientType.class) == RecipientType.TO;
	}

	public void carbonCopyTypeShouldBeConvertible() {
		assert converter.convert("cc", RecipientType.class) == RecipientType.CC;
	}

	public void blindCarbonCopyTypeShouldBeConvertible() {
		assert converter.convert("bcc", RecipientType.class) == RecipientType.BCC;
	}

	public void newsgroupsTypeShouldBeConvertible() {
		Object recipientType = converter.convert("newsgroups", RecipientType.class);
		assert recipientType == javax.mail.internet.MimeMessage.RecipientType.NEWSGROUPS;
	}

	@Test(dependsOnMethods = "toTypeShouldBeConvertible")
	public void converterShouldBeCaseInsensitive() {
		assert converter.convert("TO", RecipientType.class) == RecipientType.TO;
	}

	@Test(expectedExceptions = AluminumException.class)
	public void tryingToConvertUnknownTypeShouldCauseException() {
		converter.convert("copy", RecipientType.class);
	}
}