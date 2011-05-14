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
package com.googlecode.aluminumproject.libraries.mail.actions;

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.context.mail.MailContextProvider;
import com.googlecode.aluminumproject.libraries.mail.MailLibraryTest;
import com.googlecode.aluminumproject.templates.TemplateException;
import com.googlecode.aluminumproject.utilities.environment.EnvironmentUtilities;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeMessage;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-mail", "slow"})
public class SendTest extends MailLibraryTest {
	private GreenMail mailServer;

	@BeforeClass
	public void writeSessionPropertySet() {
		Properties sessionPropertySet = new Properties();
		sessionPropertySet.setProperty("mail.from", "from@aluminum");
		sessionPropertySet.setProperty("mail.transport.protocol", "smtp");
		sessionPropertySet.setProperty("mail.smtp.host", "localhost");
		sessionPropertySet.setProperty("mail.smtp.port", "3025");

		EnvironmentUtilities.getPropertySetContainer().writePropertySet("mail", sessionPropertySet);
	}

	protected void addConfigurationParameters(ConfigurationParameters configurationParameters) {
		configurationParameters.addParameter(MailContextProvider.SESSION_PROPERTY_SET_NAME, "mail");
	}

	@BeforeMethod
	public void startMailServer() {
		mailServer = new GreenMail(ServerSetupTest.SMTP);

		mailServer.start();
	}

	@AfterMethod
	public void stopMailServer() {
		mailServer.stop();
	}

	public void mailShouldBeSent() throws MessagingException {
		processTemplate("send");

		MimeMessage[] messages = mailServer.getReceivedMessages();
		assert messages.length == 1;

		MimeMessage message = messages[0];
		assert GreenMailUtil.getAddressList(message.getFrom()).equals("from@aluminum");
		assert GreenMailUtil.getAddressList(message.getAllRecipients()).equals("to@aluminum");
		assert message.getSubject().equals("subject");
		assert GreenMailUtil.getBody(message).equals("body");
	}

	@Test(dependsOnMethods = "mailShouldBeSent")
	public void senderShouldBeOverridable() throws MessagingException {
		processTemplate("send-from-custom-address");

		MimeMessage[] messages = mailServer.getReceivedMessages();
		assert messages.length == 1;

		MimeMessage message = messages[0];
		assert GreenMailUtil.getAddressList(message.getFrom()).equals("news@aluminum");
		assert GreenMailUtil.getAddressList(message.getAllRecipients()).equals("to@aluminum");
		assert message.getSubject().equals("subject");
		assert GreenMailUtil.getBody(message).equals("body");
	}

	@Test(dependsOnMethods = "mailShouldBeSent")
	public void mailToMultipleRecipientsShouldBeSent() throws MessagingException {
		processTemplate("send-to-multiple-recipients");

		MimeMessage[] messages = mailServer.getReceivedMessages();
		assert messages.length == 2;

		for (MimeMessage message: messages) {
			assert GreenMailUtil.getAddressList(message.getFrom()).equals("from@aluminum");
			assert GreenMailUtil.getAddressList(message.getRecipients(RecipientType.TO)).equals("to@aluminum");
			assert GreenMailUtil.getAddressList(message.getRecipients(RecipientType.CC)).equals("cc@aluminum");
			assert message.getSubject().equals("subject");
			assert GreenMailUtil.getBody(message).equals("body");
		}
	}

	@Test(dependsOnMethods = "mailShouldBeSent", expectedExceptions = TemplateException.class)
	public void omittingRecipientsShouldCauseException() {
		processTemplate("send-without-recipients");
	}
}