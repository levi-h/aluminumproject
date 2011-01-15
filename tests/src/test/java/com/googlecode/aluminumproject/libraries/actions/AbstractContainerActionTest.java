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
package com.googlecode.aluminumproject.libraries.actions;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.writers.NullWriter;
import com.googlecode.aluminumproject.writers.StringWriter;
import com.googlecode.aluminumproject.writers.Writer;
import com.googlecode.aluminumproject.writers.WriterException;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class AbstractContainerActionTest {
	public static class TextContainer extends AbstractContainerAction<String> {
		private String text;

		public TextContainer(String text) {
			this.text = text;
		}

		protected String provideContainerObject(Context context) {
			return text;
		}
	}

	public void containerObjectShouldBeAvailableAfterExecutingAction() {
		ContainerAction<String> container = new TextContainer("hunky dory");
		container.setBody(new ActionBody() {
			public void invoke(Context context, Writer writer) {}

			public ActionBody copy() {
				return this;
			}
		});
		container.execute(new DefaultContext(), new NullWriter());

		String containerObject = container.getContainerObject();
		assert containerObject != null;
		assert containerObject.equals("hunky dory");
	}

	public static class TextUser extends AbstractAction {
		public void execute(Context context, Writer writer) throws ActionException, ContextException, WriterException {
			writer.write("it's ");
			writer.write(((ContainerAction<?>) getParent()).getContainerObject());
		}
	}

	public void bodyShouldBeInvoked() {
		final Action user = new TextUser();
		ContainerAction<String> container = new TextContainer("fine");

		user.setParent(container);
		container.setBody(new ActionBody() {
			public void invoke(Context context, Writer writer)
					throws ActionException, ContextException, WriterException {
				user.execute(context, writer);
			}

			public ActionBody copy() {
				return this;
			}
		});

		StringWriter writer = new StringWriter();

		container.execute(new DefaultContext(), writer);

		String output = writer.getString();
		assert output != null;
		assert output.equals("it's fine");
	}
}