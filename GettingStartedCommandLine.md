This page helps you getting started with Aluminum when used from the command line. It assumes you're reasonably familiar with the command line.

## Setting up your environment ##

First, unpackage the distribution archive you've downloaded to a convenient location and create an environment variable named `ALUMINUM_HOME` that points to that location. After that, add the `bin` directory inside the Aluminum home directory to your path.

To check whether everything works, you can run the following command:

> _alu_

This should result in output similar to the following:

```
alu - Aluminum

Processes a template.

Usage: alu [options] [template file]

The following options are accepted:

Option                                  Description
------                                  -----------
--debug                                 Prints debug statements.
-h, --help                              Displays this help message.
-p, --parser                            The name of the parser to use.
--print-stack-traces                    Prints the stack traces of exceptions.
```

If the command can't be found, please double check your path. If you are not allowed to run the command, you might want to change the file permissions of the scripts in the `bin` directory.

## Running your first template ##

Create a file named `hello.xml` with the following contents:

```
<c:template xmlns:c="http://aluminumproject.googlecode.com/core">
    Hello!
</c:template>
```

To process the template, issue the following command:

> _alu hello.xml_

This should result in the following output:

> `Hello!`

## Reading more ##

Of course, Aluminum is capable of much more than we've shown here. Information about Aluminum and command-line usage can be found in the [complete documentation](Documentation.md).