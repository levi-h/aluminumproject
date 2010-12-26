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
package com.googlecode.aluminumproject.parsers.aluscript;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.parsers.aluscript.instructions.Instruction;
import com.googlecode.aluminumproject.templates.ActionElement;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.templates.TemplateBuilder;
import com.googlecode.aluminumproject.templates.TemplateElement;
import com.googlecode.aluminumproject.utilities.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The context that is used by the {@link AluScriptParser AluScript parser}. Per parsed template, one context will be
 * used.
 * <p>
 * The context contains a number of {@link Instruction instructions}. These instructions can access the {@link
 * #getConfiguration()}, {@link #addLibraryUrlAbbreviation(String, String) add} and {@link #getLibraryUrlAbbreviations()
 * retrieve} library URL abbreviations, and add {@link #addTemplateElement(TemplateElement) template elements}.
 * <p>
 * The library URL abbreviations are specific to a nesting level. The nesting level is controlled by the parser,
 * although it is incremented when an {@link ActionElement action element} is added.
 *
 * @author levi_h
 */
public class AluScriptContext {
	private Configuration configuration;

	private AluScriptSettings settings;

	private Map<String, Instruction> instructions;

	private TemplateBuilder templateBuilder;
	private int level;

	private Map<Integer, Map<String, String>> libraryUrlAbbreviations;

	private final Logger logger;

	/**
	 * Creates an AluScript context.
	 *
	 * @param configuration the configuration of the parser
	 * @param settings the template settings
	 * @param instructions all available instructions
	 * @throws AluScriptException when the instructions contain duplicates
	 */
	public AluScriptContext(Configuration configuration, AluScriptSettings settings,
			List<Instruction> instructions) throws AluScriptException {
		logger = Logger.get(getClass());

		this.configuration = configuration;

		this.settings = settings;

		this.instructions = new HashMap<String, Instruction>();

		for (Instruction instruction: instructions) {
			addInstruction(instruction);
		}

		templateBuilder = new TemplateBuilder();

		libraryUrlAbbreviations = new HashMap<Integer, Map<String, String>>();
	}

	/**
	 * Returns the configuration of the AluScript parser.
	 *
	 * @return the configuration with which the {@link AluScriptParser AluScript parser} was initialised
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * Returns the configured template settings.
	 *
	 * @return the template settings to use
	 */
	public AluScriptSettings getSettings() {
		return settings;
	}

	private void addInstruction(Instruction instruction) throws AluScriptException {
		String name = instruction.getName();

		if (instructions.containsKey(name)) {
			throw new AluScriptException("duplicate instruction name (", name, ")");
		} else {
			logger.debug("adding instruction (", name, ")");

			instructions.put(name, instruction);
		}
	}

	/**
	 * Finds an instruction with a certain name.
	 *
	 * @param name the name of the instruction to find
	 * @return the instruction with the given name
	 * @throws AluScriptException when no instruction with the given name can be found
	 */
	public Instruction findInstruction(String name) throws AluScriptException {
		if (instructions.containsKey(name)) {
			return instructions.get(name);
		} else {
			throw new AluScriptException("can't find instruction named '", name, "'");
		}
	}

	/**
	 * Adds a template element at the current nesting level.
	 *
	 * @param templateElement the template element to add
	 */
	public void addTemplateElement(TemplateElement templateElement) {
		logger.debug("adding template element ", templateElement, " at level ", level);

		templateBuilder.addTemplateElement(templateElement);

		if (templateElement instanceof ActionElement) {
			level++;
		} else {
			templateBuilder.restoreCurrentTemplateElement();
		}
	}

	/**
	 * Returns the current nesting level.
	 *
	 * @return the current nesting level of this AluScript context
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Sets the current nesting level. It should not exceed the current nesting level with more than one.
	 *
	 * @param level the nesting level to use
	 * @throws AluScriptException when the given nesting level is too deep
	 */
	public void setLevel(int level) throws AluScriptException {
		if (level > this.level) {
			throw new AluScriptException("unexpected nesting level (", level, "), expected ", this.level, " or less");
		} else if (level < this.level) {
			while (this.level > level) {
				templateBuilder.restoreCurrentTemplateElement();

				this.level--;
			}

			logger.debug("new level: ", level);
		}
	}

	/**
	 * Adds a library URL abbreviation that can be used by template elements at the current nesting level.
	 *
	 * @param abbreviation the abbreviation of the library URL to add
	 * @param libraryUrl the library URL to add
	 * @throws AluScriptException when a library with the given abbreviation has already been added for the current
	 *                            nesting level
	 */
	public void addLibraryUrlAbbreviation(String abbreviation, String libraryUrl) throws AluScriptException {
		Map<String, String> libraryUrlAbbreviations = getOrCreateLibraryUrlAbbreviations(level);

		if (libraryUrlAbbreviations.containsKey(abbreviation)) {
			throw new AluScriptException("duplicate library URL (", abbreviation, ": ", libraryUrl, ")");
		} else {
			logger.debug("adding library URL at level ", level, " (", abbreviation, ": ", libraryUrl, ")");

			libraryUrlAbbreviations.put(abbreviation, libraryUrl);
		}
	}

	/**
	 * Returns all library URLs and their abbreviations for the current nesting level.
	 *
	 * @return all library URL abbreviations that can be used by template elements at the current nesting level
	 */
	public Map<String, String> getLibraryUrlAbbreviations() {
		Map<String, String> libraryUrlAbbreviations = new HashMap<String, String>();

		for (int level = 0; level <= this.level; level++) {
			libraryUrlAbbreviations.putAll(getOrCreateLibraryUrlAbbreviations(level));
		}

		return libraryUrlAbbreviations;
	}

	private Map<String, String> getOrCreateLibraryUrlAbbreviations(int level) {
		if (!libraryUrlAbbreviations.containsKey(level)) {
			libraryUrlAbbreviations.put(level, new HashMap<String, String>());
		}

		return libraryUrlAbbreviations.get(level);
	}

	/**
	 * Builds and returns the template.
	 *
	 * @return the built template
	 */
	public Template getTemplate() {
		setLevel(0);

		return templateBuilder.build();
	}
}