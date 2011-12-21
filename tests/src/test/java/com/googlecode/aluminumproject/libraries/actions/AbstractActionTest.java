/*
 * Copyright 2009-2011 Levi Hoogenberg
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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.writers.AbstractDecorativeWriter;
import com.googlecode.aluminumproject.writers.NullWriter;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class AbstractActionTest {
	public static class Grandparent extends AbstractContainerAction<Integer> {
		@Override
		public void execute(Context context, Writer writer) throws AluminumException {
			writer.write("I'm the grandparent!");
		}

		protected Integer provideContainerObject(Context context) {
			return 10;
		}
	}

	public static class Parent extends AbstractContainerAction<Float> {
		@Override
		public void execute(Context context, Writer writer) throws AluminumException {
			writer.write("I'm the parent!");
		}

		protected Float provideContainerObject(Context context) {
			return 10F;
		}
	}

	public static class Action extends AbstractAction {
		public void execute(Context context, Writer writer) throws AluminumException {
			writer.write("I'm the action itself!");
		}
	}

	private AbstractAction grandparent;
	private AbstractAction parent;
	private AbstractAction action;

	private ActionBody parentBody;

	@BeforeMethod
	public void createActions() {
		grandparent = new Grandparent();

		parent = new Parent();
		parent.setParent(grandparent);

		action = new Action();
		action.setParent(parent);

		parentBody = new ActionBody() {
			public void invoke(Context context, Writer writer) throws AluminumException {
				action.execute(context, writer);
			}

			public ActionBody copy() {
				return this;
			}
		};
		parent.setBody(parentBody);
	}

	public void findingAncestorShouldFindActionWithRequestedType() {
		assert parent.findAncestorOfType(Grandparent.class) == grandparent;
		assert action.findAncestorOfType(Grandparent.class) == grandparent;
	}

	public void findingAncestorShouldFindFirstAncestorIfMoreThanOneAncestorMatch() {
		assert action.findAncestorOfType(AbstractAction.class) == parent;
	}

	public void findingAncestorWithUnknownTypeShouldResultInNull() {
		assert action.findAncestorOfType(Action.class) == null;
	}

	public void findingContainerAncestorShouldFindActionWithRequestedContainerType() {
		assert parent.findAncestorContainingType(Integer.class) == grandparent;
		assert action.findAncestorContainingType(Integer.class) == grandparent;
	}

	public void findingContainerAncestorShouldFindFirstAncestorIfMoreThanOneAncestorMatch() {
		assert action.findAncestorContainingType(Number.class) == parent;
	}

	public void findingContainerAncestorWithUnknownContainerTypeShouldResultInNull() {
		assert action.findAncestorContainingType(String.class) == null;
	}

	public void parentShouldBeObtainable() {
		assert parent.getParent() == grandparent;
		assert action.getParent() == parent;
	}

	public void gettingParentOfActionThatHasNoParentShouldResultInNull() {
		assert grandparent.getParent() == null;
	}

	public void bodyShouldBeObtainable() {
		assert parentBody == parent.getBody();
	}

	@Test(dependsOnMethods = "bodyShouldBeObtainable")
	public void bodyListShouldBeObtainable() {
		List<?> bodyList = parent.getBodyList(new DefaultContext(), new NullWriter());
		assert bodyList != null;
		assert bodyList.size() == 1;
		assert bodyList.contains("I'm the action itself!");
	}

	@Test(dependsOnMethods = "bodyListShouldBeObtainable")
	public void obtainingBodyListShouldRespectDecorativeWriters() {
		List<?> bodyList = parent.getBodyList(new DefaultContext(), new AbstractDecorativeWriter(new NullWriter()) {
			public void write(Object object) throws AluminumException {
				checkOpen();

				getWriter().write(((String) object).toUpperCase());
			}
		});
		assert bodyList != null;
		assert bodyList.size() == 1;
		assert bodyList.contains("I'M THE ACTION ITSELF!");
	}

	@Test(dependsOnMethods = "bodyShouldBeObtainable")
	public void bodyObjectShouldBeObtainable() {
		String bodyObject = parent.getBodyObject(String.class, new DefaultContext(), new NullWriter());
		assert bodyObject != null;
		assert bodyObject.equals("I'm the action itself!");
	}

	@Test(dependsOnMethods = "bodyObjectShouldBeObtainable", expectedExceptions = AluminumException.class)
	public void obtainingBodyObjectWithWrongTypeShouldCauseException() {
		parent.getBodyObject(Integer.class, new DefaultContext(), new NullWriter());
	}

	@Test(dependsOnMethods = "bodyObjectShouldBeObtainable", expectedExceptions = AluminumException.class)
	public void obtainingBodyObjectFromActionWhoseBodyDoesNotWriteAnythingShouldCauseException() {
		action.setBody(new ActionBody() {
			public void invoke(Context context, Writer writer) {}

			public ActionBody copy() {
				return this;
			}
		});

		action.getBodyObject(Object.class, new DefaultContext(), new NullWriter());
	}

	@Test(dependsOnMethods = "bodyObjectShouldBeObtainable", expectedExceptions = AluminumException.class)
	public void obtainingBodyObjectFromActionWhoseBodyWritesMultipleObjectsShouldCauseException() {
		action.setBody(new ActionBody() {
			public void invoke(Context context, Writer writer) {
				for (int i = 1; i <= 5; i++) {
					writer.write(i);
				}
			}

			public ActionBody copy() {
				return this;
			}
		});

		action.getBodyObject(Integer.class, new DefaultContext(), new NullWriter());
	}

	@Test(dependsOnMethods = "bodyShouldBeObtainable")
	public void bodyTextShouldBeObtainable() {
		String bodyText = parent.getBodyText(new DefaultContext(), new NullWriter());
		assert bodyText != null;
		assert bodyText.equals("I'm the action itself!");
	}

	@Test(dependsOnMethods = "bodyTextShouldBeObtainable")
	public void obtainingBodyTextShouldRespectDecorativeWriters() {
		String bodyText = parent.getBodyText(new DefaultContext(), new AbstractDecorativeWriter(new NullWriter()) {
			public void write(Object object) throws AluminumException {
				checkOpen();

				getWriter().write(String.format("*%s*", object));
			}
		});
		assert bodyText != null;
		assert bodyText.equals("*I'm the action itself!*"): bodyText;
	}
}