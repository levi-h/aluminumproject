/*
 * Copyright 2009-2010 Levi Hoogenberg
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
package com.googlecode.aluminumproject.expressions.el;

import com.googlecode.aluminumproject.Logger;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELResolver;

/**
 * An {@link ELResolver EL resolver} that uses the {@link Context context} to find implicit objects.
 *
 * @author levi_h
 */
@Ignored
public class ImplicitObjectElResolver extends ELResolver {
	private final Logger logger;

	/**
	 * Creates an implicit object EL resolver.
	 */
	public ImplicitObjectElResolver() {
		logger = Logger.get(getClass());
	}

	@Override
	public Object getValue(ELContext elContext, Object base, Object property) {
		Object value = null;

		if (handles(elContext, base, property)) {
			Context context = ((ElContext) elContext).getContext();
			String name = (String) property;

			if (context.getImplicitObjectNames().contains(name)) {
				try {
					value = context.getImplicitObject(name);

					elContext.setPropertyResolved(true);
				} catch (ContextException exception) {
					logger.debug("can't find implicit object '", property, "'");
				}
			}
		}

		return value;
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		return null;
	}

	@Override
	public void setValue(ELContext context, Object base, Object property, Object value) {}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		boolean readOnly;

		if (handles(context, base, property)) {
			readOnly = true;

			context.setPropertyResolved(true);
		} else {
			readOnly = false;
		}

		return readOnly;
	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
		return null;
	}

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		return null;
	}

	private boolean handles(ELContext elContext, Object base, Object property) {
		return (elContext instanceof ElContext) && (base == null) && (property instanceof String);
	}
}