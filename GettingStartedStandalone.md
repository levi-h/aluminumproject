This page helps you getting started with Aluminum when used standalone.

## Collecting all needed libraries ##

The distribution contains a directory named `lib`; this directory contains the Aluminum modules and their dependencies. For an overview of the modules and their dependencies, see [this page](AluminumModulesAndTheirDependencies.md). For this example, you'll need the _core_, _el-expressions_, _core-library_, and _xml-parser_ modules with their dependencies.

Aluminum uses [SLF4J](http://slf4j.org/) for its logging, which means that you'll need an SLF4J implementation as well. This can be an adapter for [Apache log4j](http://logging.apache.org/log4j/) or [JDK logging](http://java.sun.com/javase/6/docs/api/java/util/logging/package-summary.html) if you're using one of those, or a standalone implementation such as [Logback](http://logback.qos.ch/). For this guide, the easiest thing to do is using the Logback JAR files from the `lib` directory.

## Writing a template ##

Create a file named `hello.xml` with the following contents:

```
<c:template xmlns:c="http://aluminumproject.googlecode.com/core">
    Hello, ${name}!
</c:template>
```

This template will print a greeting; the `${name}` expression will be replaced with the context variable named _name_.

## Processing the template ##

To process the template, we'll write a small application. Create a file with the name `Hello.java` and add the following:

```
package com.googlecode.aluminumproject.examples;

import com.googlecode.aluminumproject.Aluminum;
import com.googlecode.aluminumproject.configuration.DefaultConfiguration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.writers.OutputStreamWriter;
import com.googlecode.aluminumproject.writers.Writer;

public class Hello {
    public static void main(String... parameters) {
        Context context = new DefaultContext();
        context.setVariable("name", System.getProperty("user.name"));

        Writer writer = new OutputStreamWriter(System.out);

        Aluminum engine = new Aluminum(new DefaultConfiguration());
        engine.processTemplate("hello.xml", "xml", context, writer);
    }
}
```

The following things happen in this class:

  * A default context is being created with a variable that contains the name of the person to greet;
  * The writer that the output of the template will be written to is being instantiated;
  * A template engine is being created - it uses a default configuration;
  * The template is being processed.

Assuming that both the template and all needed libraries are on the class path, running the class should result in output similar to the following:

> `Hello, you!`

## Reading more ##

Of course, Aluminum can do much more than what we've demonstrated with this example. Please consult the [complete documentation](Documentation.md) for more information.