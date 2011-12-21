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
package com.googlecode.aluminumproject.cli.commands.aludoc;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.cli.Command;
import com.googlecode.aluminumproject.cli.OptionEffect;

import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 * Generates library documentation.
 * <p>
 * The <em>aludoc</em> command accepts the following options:
 * <ul>
 * <li>The location option ({@code --location}): where the documentation will be generated to - the default location is
 *     the current directory;
 * <li>The exclude option ({@code -x} or {@code --exclude}): adds a library to the list of libraries for which no
 *     documentation will be generated.
 * </ul>
 *
 * @author levi_h
 */
public class AluDoc extends Command {
	private File location;

	private List<String> excludes;

	/**
	 * Creates an <em>aludoc</em> command.
	 */
	AluDoc() {
		location = new File(".");

		excludes = new LinkedList<String>();

		addOptions();
	}

	private void addOptions() {
		String locationDescription = "The location of the generated documentation.";

		OptionSpec<File> location =
			getOptionParser().accepts("location", locationDescription).withRequiredArg().ofType(File.class);

		addOption(location, new OptionEffect<File>() {
			public void apply(OptionSet options, OptionSpec<File> option) {
				AluDoc.this.location = option.value(options);
			}
		});

		List<String> excludeOptions = Arrays.asList("x", "exclude");
		String excludeDescription = "The URL of a library for which no documentation should be generated.";

		OptionSpec<String> excludes =
			getOptionParser().acceptsAll(excludeOptions, excludeDescription).withRequiredArg().ofType(String.class);

		addOption(excludes, new OptionEffect<String>() {
			public void apply(OptionSet options, OptionSpec<String> option) {
				AluDoc.this.excludes.addAll(option.values(options));
			}
		});
	}

	@Override
	protected Map<String, String> getHelpInformation() {
		Map<String, String> helpInformation = new HashMap<String, String>();

		helpInformation.put("name", "aludoc - AluDoc");
		helpInformation.put("description", "Generates library documentation.");
		helpInformation.put("usage", "Usage: aludoc [options]");

		return helpInformation;
	}

	@Override
	protected void execute(
			PrintStream outputStream, PrintStream errorStream, List<String> arguments) throws AluminumException {
		com.googlecode.aluminumproject.tools.aludoc.AluDoc aluDoc =
			new com.googlecode.aluminumproject.tools.aludoc.AluDoc(location);

		for (String exclude: excludes) {
			aluDoc.excludeLibrary(exclude);
		}

		aluDoc.generate();
	}


	/**
	 * Executes the <em>aludoc</em> command.
	 *
	 * @param parameters the command-line parameters
	 */
	public static void main(String... parameters) {
		System.exit(new AluDoc().execute(System.out, System.err, parameters));
	}
}