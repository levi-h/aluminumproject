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
package com.googlecode.aluminumproject.utilities.finders;

import com.googlecode.aluminumproject.utilities.finders.MethodFinder.MethodFilter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"utilities", "fast"})
public class MethodFinderTest {
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void notSupplyingTypeShouldCauseException() {
		MethodFilter acceptFilter = new MethodFilter() {
			public boolean accepts(Method method) {
				return true;
			}
		};

		MethodFinder.find(acceptFilter, null);
	}

	public void methodsInClassShouldBeFindable() throws NoSuchMethodException {
		MethodFilter stringReturningMethodFilter = new MethodFilter() {
			public boolean accepts(Method method) {
				return method.getReturnType() == String.class;
			}
		};

		List<Method> stringReturningMethods = MethodFinder.find(stringReturningMethodFilter, Object.class);
		assert stringReturningMethods != null;
		assert stringReturningMethods.contains(Object.class.getMethod("toString"));
	}

	@Test(dependsOnMethods = "methodsInClassShouldBeFindable")
	public void nonPublicMethodsInClassShouldBeFindable() throws NoSuchMethodException {
		MethodFilter protectedMethodFilter = new MethodFilter() {
			public boolean accepts(Method method) {
				return Modifier.isProtected(method.getModifiers());
			}
		};

		List<Method> protectedMethods = MethodFinder.find(protectedMethodFilter, Object.class);
		assert protectedMethods != null;
		assert protectedMethods.contains(Object.class.getDeclaredMethod("clone"));
		assert protectedMethods.contains(Object.class.getDeclaredMethod("finalize"));
	}

	@Test(dependsOnMethods = "methodsInClassShouldBeFindable")
	public void staticMethodsInClassShouldBeFindable() throws NoSuchMethodException {
		MethodFilter staticMethodFilter = new MethodFilter() {
			public boolean accepts(Method method) {
				return Modifier.isStatic(method.getModifiers());
			}
		};

		List<Method> staticMethods = MethodFinder.find(staticMethodFilter, Class.class);
		assert staticMethods != null;
		assert staticMethods.contains(Class.class.getMethod("forName", String.class));
	}

	@Test(dependsOnMethods = "methodsInClassShouldBeFindable")
	public void methodsInSuperclassShouldBeFindable() throws NoSuchMethodException {
		MethodFilter nativeMethodFilter = new MethodFilter() {
			public boolean accepts(Method method) {
				return Modifier.isNative(method.getModifiers());
			}
		};

		List<Method> nativeMethods = MethodFinder.find(nativeMethodFilter, Integer.class);
		assert nativeMethods != null;
		assert nativeMethods.contains(Object.class.getMethod("notifyAll"));
	}

	public void methodsInInterfaceShouldBeFindable() throws NoSuchMethodException {
		MethodFilter nameEndsWithNextMethodFilter = new MethodFilter() {
			public boolean accepts(Method method) {
				return method.getName().matches("(?i).*next");
			}
		};

		List<Method> nameEndsWithNextMethods = MethodFinder.find(nameEndsWithNextMethodFilter, Iterator.class);
		assert nameEndsWithNextMethods != null;
		assert nameEndsWithNextMethods.contains(Iterator.class.getMethod("hasNext"));
		assert nameEndsWithNextMethods.contains(Iterator.class.getMethod("next"));
	}

	@Test(dependsOnMethods = "methodsInInterfaceShouldBeFindable")
	public void methodsInSuperinterfaceShouldBeFindable() throws NoSuchMethodException {
		MethodFilter removeMethodFilter = new MethodFilter() {
			public boolean accepts(Method method) {
				return method.getName().equals("remove");
			}
		};

		List<Method> removeMethods = MethodFinder.find(removeMethodFilter, ListIterator.class);
		assert removeMethods != null;
		assert removeMethods.contains(Iterator.class.getMethod("remove"));
	}
}