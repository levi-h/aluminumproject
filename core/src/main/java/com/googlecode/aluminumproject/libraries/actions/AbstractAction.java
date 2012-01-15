/*
 * Copyright 2009-2012 Aluminum project
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

import static com.googlecode.aluminumproject.utilities.GenericsUtilities.getTypeArgument;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.utilities.Logger;
import com.googlecode.aluminumproject.utilities.Utilities;
import com.googlecode.aluminumproject.writers.DecorativeWriter;
import com.googlecode.aluminumproject.writers.ListWriter;
import com.googlecode.aluminumproject.writers.StringWriter;
import com.googlecode.aluminumproject.writers.TextWriter;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.List;

/**
 * Reduces the effort it takes to implement {@link Action the Action interface} and offers some convenience methods.
 */
public abstract class AbstractAction implements Action {
	private @Ignored Action parent;
	private @Ignored ActionBody body;

	/** The logger to use. */
	protected final Logger logger;

	/**
	 * Creates an abstract action.
	 */
	protected AbstractAction() {
		logger = Logger.get(getClass());
	}

	public Action getParent() {
		return parent;
	}

	/**
	 * Finds an action with a certain type in the ancestor chain of this action.
	 *
	 * @param <T> the requested action type
	 * @param type the type of the ancestor to find
	 * @return the first ancestor with the given type or {@code null} if none of this action's ancestors has the
	 *         requested type
	 */
	protected <T> T findAncestorOfType(Class<T> type) {
		Action action = getParent();

		while ((action != null) && !type.isAssignableFrom(action.getClass())) {
			action = action.getParent();
		}

		return type.cast(action);
	}

	/**
	 * Finds an action with a certain container type in the ancestor chain of this action.
	 *
	 * @param <T> the requested container type
	 * @param type the type of the container object of the action to find
	 * @return the first ancestor that contains objects with the given type or {@code null} if no such action can be
	 *         found
	 */
	protected <T> ContainerAction<T> findAncestorContainingType(Class<T> type) {
		Action action = getParent();
		boolean found = false;

		while ((action != null) && !found) {
			if ((action instanceof ContainerAction) &&
					type.isAssignableFrom(getTypeArgument(action.getClass(), ContainerAction.class, 0))) {
				found = true;
			} else {
				action = action.getParent();
			}
		}

		return Utilities.typed(action);
	}

	public void setParent(Action parent) {
		this.parent = parent;
	}

	/**
	 * Returns the body of this action.
	 *
	 * @return this action's body
	 */
	protected ActionBody getBody() {
		return body;
	}

	/**
	 * Invokes the body of this action, capturing its output.
	 *
	 * @param context the context to execute the body in
	 * @param writer the writer that would normally have been used to invoke the action body with
	 * @return the objects that were written by the body
	 * @throws AluminumException when the body can't be invoked
	 */
	protected List<?> getBodyList(Context context, Writer writer) throws AluminumException {
		ListWriter listWriter = new ListWriter(true);

		invokeBody(context, writer, listWriter);

		return listWriter.getList();
	}

	/**
	 * Invokes the body of this action, capturing its output, which is expected to be a single object of a certain type.
	 * If the body contains no objects, more than one object, or an object of an unexpected type, this method will throw
	 * an exception.
	 *
	 * @param <T> the type of the body object
	 * @param type the expected body object type
	 * @param context the context to execute the body in
	 * @param writer the writer that would normally have been used to invoke the action body with
	 * @return the object that was written by the body
	 * @throws AluminumException when the body can't be invoked or when it does not write exactly one object of the
	 *                           expected type
	 */
	protected <T> T getBodyObject(Class<T> type, Context context, Writer writer) throws AluminumException {
		List<?> bodyList = getBodyList(context, writer);

		if (bodyList.isEmpty()) {
			throw new AluminumException("no body objects were written, expected one of type ", type.getName());
		} else if (bodyList.size() > 1) {
			throw new AluminumException("multiple objects were written, expected single one of type ", type.getName());
		} else {
			Object bodyObject = bodyList.get(0);

			if (type.isInstance(bodyObject)) {
				return type.cast(bodyObject);
			} else {
				throw new AluminumException(
					"body object ", bodyObject, " does not have expected type ", type.getName());
			}
		}
	}

	/**
	 * Invokes the body of this action, capturing its output as text.
	 *
	 * @param context the context to execute the body in
	 * @param writer the writer that would normally have been used to invoke the action body with
	 * @return the text that was written by the body
	 * @throws AluminumException when the body can't be invoked
	 */
	protected String getBodyText(Context context, Writer writer) throws AluminumException {
		StringWriter stringWriter = new StringWriter();

		invokeBody(context, writer, new TextWriter(stringWriter, true));

		return stringWriter.getString();
	}

	/**
	 * Invokes the body of this action with a writer, inheriting all decorative writers of the original writer.
	 *
	 * @param context the context to invoke the body with
	 * @param originalWriter the writer that was passed to the {@link #execute(Context, Writer) execute method}
	 * @param writer the writer to invoke the body with
	 * @throws AluminumException when the body can't be invoked
	 */
	protected void invokeBody(Context context, Writer originalWriter, Writer writer) throws AluminumException {
		DecorativeWriter decorativeWriter = getInnermostDecorativeWriter(originalWriter);

		if (decorativeWriter == null) {
			getBody().invoke(context, writer);
		} else {
			Writer underlyingWriter = decorativeWriter.getWriter();

			try {
				decorativeWriter.setWriter(writer);

				getBody().invoke(context, originalWriter);
			} finally {
				decorativeWriter.setWriter(underlyingWriter);
			}
		}
	}

	private DecorativeWriter getInnermostDecorativeWriter(Writer writer) {
		DecorativeWriter decorativeWriter = null;

		while (writer instanceof DecorativeWriter) {
			decorativeWriter = (DecorativeWriter) writer;

			writer = decorativeWriter.getWriter();
		}

		return decorativeWriter;
	}

	public void setBody(ActionBody body) {
		this.body = body;
	}
}