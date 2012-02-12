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
package com.googlecode.aluminumproject.expressions.el;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.converters.ConverterRegistry;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.LibraryInformation;
import com.googlecode.aluminumproject.libraries.functions.ConstantFunctionArgument;
import com.googlecode.aluminumproject.libraries.functions.Function;
import com.googlecode.aluminumproject.libraries.functions.FunctionArgument;
import com.googlecode.aluminumproject.libraries.functions.FunctionFactory;
import com.googlecode.aluminumproject.utilities.ConfigurationUtilities;
import com.googlecode.aluminumproject.utilities.Logger;
import com.googlecode.aluminumproject.utilities.StringUtilities;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;

/**
 * Creates {@link Method delegates} that call {@link Function functions}. These delegates are needed since the {@link
 * FunctionMapper EL function mapper} expects functions to be static methods. In Aluminum, however, a function is an
 * interface - it does not necessarily have to be implemented using a static method.
 * <p>
 * There's one function delegate factory per JVM. The delegates are created using
 * <a href="www.csg.is.titech.ac.jp/~chiba/javassist/">Javassist</a>; for each configuration, a separate function class
 * is created. These classes contain a method per {@link FunctionFactory function factory}. When the method is invoked,
 * the parameters are converted into {@link FunctionArgument function arguments} and used to create a function. Finally,
 * the function is called with the current {@link Context context}.
 */
public class FunctionDelegateFactory {
	private FunctionDelegateFactory() {}

	/**
	 * Adds a configuration for which a function class will be created.
	 *
	 * @param configuration the configuration to add
	 */
	public static void addConfiguration(Configuration configuration) {
		delegateRegistries.put(configuration, new DelegateRegistry(configuration, nextClassIndex.getAndIncrement()));
	}

	/**
	 * Removes a configuration for which a function class has been created.
	 *
	 * @param configuration the configuration to remove
	 */
	public static void removeConfiguration(Configuration configuration) {
		delegateRegistries.remove(configuration);

		functionContexts.get().clear();
	}

	/**
	 * Finds a delegate to a function.
	 *
	 * @param configuration the configuration to use
	 * @param libraryUrl the URL of the library that contains the function
	 * @param functionName the name of the function
	 * @param context the context that the function will be invoked in
	 * @return a delegate to the requested function or {@code null} if the delegate can't be found
	 */
	public static Method findDelegate(
			Configuration configuration, String libraryUrl, String functionName, Context context) {
		Method delegate = null;

		DelegateRegistry delegateRegistry = delegateRegistries.get(configuration);

		if (delegateRegistry == null) {
			logger.warn("configuration ", configuration, " was not added");
		} else {
			try {
				delegateRegistry.initialise();
			} catch (CannotCompileException exception) {
				logger.warn(exception, "can't create delegates");
			}

			FunctionFactory functionFactory = delegateRegistry.getFunctionFactory(getKey(libraryUrl, functionName));

			if (functionFactory == null) {
				Library library = ConfigurationUtilities.findLibrary(configuration, libraryUrl);

				if ((library != null) && library.getInformation().isSupportingDynamicFunctions()) {
					try {
						functionFactory = library.getDynamicFunctionFactory(functionName);
					} catch (AluminumException exception) {
						logger.warn("can't get dynamic function factory for function '", functionName, "'",
							" in library with URL '", libraryUrl, "'");
					}
				} else {
					logger.warn("can't find function with library URL '", libraryUrl, "' and name '", functionName, "'",
						(library == null) ? "" : " and dynamic functions are not supported by the library");
				}
			}

			if (functionFactory != null) {
				try {
					delegate = delegateRegistry.getDelegate(functionFactory, libraryUrl, functionName);

					functionContexts.get().offer(new FunctionContext(configuration, functionFactory, context));
				} catch (CannotCompileException exception) {
					logger.warn(exception, "can't find dynamic delegate");
				} catch (NoSuchMethodException exception) {
					logger.warn(exception, "can't find delegate");
				}
			}
		}

		return delegate;
	}

	/**
	 * Calls a function. This method is invoked by a delegate.
	 *
	 * @param key the key of the delegate
	 * @param parameters the parameters that were given to the delegate
	 * @return the function result
	 * @throws AluminumException when the function can't be created
	 */
	public static Object callFunction(String key, Object[] parameters) throws AluminumException {
		FunctionContext functionContext = null;

		Queue<FunctionContext> functionContexts = FunctionDelegateFactory.functionContexts.get();

		while ((functionContext == null) && !functionContexts.isEmpty()) {
			functionContext = functionContexts.remove();

			String libraryUrl = functionContext.functionFactory.getLibrary().getInformation().getUrl();
			String functionName = functionContext.functionFactory.getInformation().getName();

			if (!key.equals(getKey(libraryUrl, functionName))) {
				functionContext = null;
			}
		}

		if (functionContext == null) {
			throw new AluminumException("can't find function context for function");
		}

		List<FunctionArgument> arguments = new ArrayList<FunctionArgument>(parameters.length);

		for (int i = 0; i < parameters.length; i++) {
			Object parameter = parameters[i];
			ConverterRegistry converterRegistry = functionContext.configuration.getConverterRegistry();

			arguments.add(new ConstantFunctionArgument(parameter, converterRegistry));
		}

		Function function = functionContext.functionFactory.create(arguments, functionContext.context);
		function.setFactory(functionContext.functionFactory);

		return function.call(functionContext.context);
	}

	private static String getKey(String libraryUrl, String functionName) {
		return String.format("%s/%s", libraryUrl, StringUtilities.camelCase(functionName));
	}

	private static AtomicInteger nextClassIndex;
	private static Map<Configuration, DelegateRegistry> delegateRegistries;

	private static ThreadLocal<Queue<FunctionContext>> functionContexts;

	private static final Logger logger;

	static {
		nextClassIndex = new AtomicInteger();
		delegateRegistries = new IdentityHashMap<Configuration, DelegateRegistry>();

		functionContexts = new ThreadLocal<Queue<FunctionContext>>() {
			@Override
			protected Queue<FunctionContext> initialValue() {
				return new LinkedList<FunctionContext>();
			}
		};

		logger = Logger.get(FunctionDelegateFactory.class);
	}

	private static class DelegateRegistry {
		private Configuration configuration;
		private int classIndex;

		private AtomicBoolean initialised;

		private Map<String, FunctionFactory> functionFactories;
		private Map<String, Integer> delegateIndices;

		private Class<?> delegates;

		private ConcurrentMap<String, Class<?>> dynamicDelegates;

		public DelegateRegistry(Configuration configuration, int classIndex) {
			this.configuration = configuration;
			this.classIndex = classIndex;

			initialised = new AtomicBoolean();
		}

		public void initialise() throws CannotCompileException {
			if (initialised.compareAndSet(false, true)) {
				functionFactories = new HashMap<String, FunctionFactory>();
				delegateIndices = new HashMap<String, Integer>();

				ClassPool classPool = new ClassPool();
				classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));

				CtClass ctFunctions = classPool.makeClass(
					String.format("com.googlecode.aluminumproject.expressions.el.Delegates%d", classIndex));

				int delegateIndex = 0;

				for (Library library: configuration.getLibraries()) {
					LibraryInformation libraryInformation = library.getInformation();

					for (String url: Arrays.asList(libraryInformation.getUrl(), libraryInformation.getVersionedUrl())) {
						for (FunctionFactory functionFactory: library.getFunctionFactories()) {
							String key =
								FunctionDelegateFactory.getKey(url, functionFactory.getInformation().getName());

							functionFactories.put(key, functionFactory);

							String delegate = getDelegate(key, functionFactory, delegateIndex);
							ctFunctions.addMethod(CtNewMethod.make(delegate, ctFunctions));

							delegateIndices.put(key, delegateIndex);
							delegateIndex++;
						}
					}
				}

				delegates = ctFunctions.toClass();

				dynamicDelegates = new ConcurrentHashMap<String, Class<?>>();
			}
		}

		private String getDelegate(String key, FunctionFactory functionFactory, int index) {
			StringBuilder parameterBuilder = new StringBuilder();
			StringBuilder parameterAssignmentBuilder = new StringBuilder();

			int parameterCount = functionFactory.getInformation().getArgumentInformation().size();

			for (int i = 0; i < parameterCount; i++) {
				if (i > 0) {
					parameterBuilder.append(", ");
				}

				parameterBuilder.append("Object p").append(i);
				parameterAssignmentBuilder.append("parameters[").append(i).append("] = p").append(i).append(";");
			}

			return StringUtilities.join(
				"public static Object d", index, "(", parameterBuilder.toString(), ") {",
				"    Object[] parameters = new Object[", parameterCount, "];",
				"",
				"    ", parameterAssignmentBuilder.toString(),
				"",
				"    return ", FunctionDelegateFactory.class.getName(), ".callFunction(\"", key, "\", parameters);",
				"}");
		}

		public FunctionFactory getFunctionFactory(String key) {
			return functionFactories.get(key);
		}

		public Method getDelegate(FunctionFactory functionFactory, String libraryUrl, String functionName)
				throws CannotCompileException, NoSuchMethodException {
			Class<?> delegates;
			int delegateIndex;

			String key = getKey(libraryUrl, functionName);

			if (delegateIndices.containsKey(key)) {
				delegates = this.delegates;
				delegateIndex = delegateIndices.get(key);
			} else {
				delegateIndex = 0;

				if (!dynamicDelegates.containsKey(functionName)) {
					synchronized (dynamicDelegates) {
						if (!dynamicDelegates.containsKey(functionName)) {
							String dynamicFunctionsClassNameFormat =
								"com.googlecode.aluminumproject.expressions.el.Delegates%dDynamicFunctions%d";
							CtClass ctDynamicFunctions = ClassPool.getDefault().makeClass(
								String.format(dynamicFunctionsClassNameFormat, classIndex, dynamicDelegates.size()));

							functionFactories.put(key, functionFactory);

							String delegate = getDelegate(key, functionFactory, delegateIndex);
							ctDynamicFunctions.addMethod(CtNewMethod.make(delegate, ctDynamicFunctions));

							dynamicDelegates.put(functionName, ctDynamicFunctions.toClass());
						}
					}
				}

				delegates = dynamicDelegates.get(functionName);
			}

			int parameterCount = functionFactory.getInformation().getArgumentInformation().size();
			Class<?>[] parameterClasses = new Class<?>[parameterCount];
			Arrays.fill(parameterClasses, Object.class);

			return delegates.getMethod(String.format("d%d", delegateIndex), parameterClasses);
		}
	}

	private static class FunctionContext {
		public Configuration configuration;
		public FunctionFactory functionFactory;
		public Context context;

		public FunctionContext(Configuration configuration, FunctionFactory functionFactory, Context context) {
			this.configuration = configuration;
			this.functionFactory = functionFactory;
			this.context = context;
		}
	}
}