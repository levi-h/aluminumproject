/*
 * Copyright 2009-2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.core.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

@SuppressWarnings("javadoc")
public class Each extends AbstractAction {
	private @Required Iterable<?> elements;

	private String elementName;
	private String informationName;

	public Each() {
		elementName = DEFAULT_ELEMENT_NAME;
		informationName = DEFAULT_INFORMATION_NAME;
	}

	public void execute(Context context, Writer writer) throws AluminumException {
		String scope = context.addScope(EACH_SCOPE, false);

		ElementProvider elementProvider = new ElementProvider(elements.iterator());

		while (elementProvider.hasNextElement()) {
			Object element = elementProvider.getNextElement();

			context.setVariable(scope, elementName, element);
			context.setVariable(scope, informationName, new EachInformation(elementProvider));

			logger.debug("invoking body with ", element);

			getBody().invoke(context, writer);
		}

		context.removeScope(scope);
	}

	private static class EachInformation implements LoopInformation {
		private ElementProvider elementProvider;

		private int index;
		private boolean last;

		public EachInformation(ElementProvider elementProvider) {
			this.elementProvider = elementProvider;

			index = elementProvider.getNextIndex() - 1;
			last = !elementProvider.hasNextElement();
		}

		public int getIndex() {
			return index;
		}

		public int getCount() {
			return elementProvider.getCount();
		}

		public boolean isFirst() {
			return index == 0;
		}

		public boolean isLast() {
			return last;
		}
	}

	private static class ElementProvider {
		private Iterator<?> iterator;

		private SortedMap<Integer, Object> elements;

		private int index;
		private int count;

		public ElementProvider(Iterator<?> iterator) {
			this.iterator = iterator;

			count = -1;
		}

		public boolean hasNextElement() {
			return (elements == null) ? iterator.hasNext() : index < count;
		}

		public Object getNextElement() {
			Object element = (elements == null) ? iterator.next() : elements.remove(index);

			index++;

			return element;
		}

		public int getNextIndex() {
			return index;
		}

		public int getCount() {
			if (count == -1) {
				elements = new TreeMap<Integer, Object>();

				if (iterator.hasNext()) {
					int i = index;

					while (iterator.hasNext()) {
						elements.put(i++, iterator.next());
					}

					count = i;
				} else {
					count = index;
				}
			}

			return count;
		}
	}

	public final static String EACH_SCOPE = "each";

	public final static String DEFAULT_ELEMENT_NAME = "element";
	public final static String DEFAULT_INFORMATION_NAME = "loop";
}