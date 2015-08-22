This page helps you getting started with Aluminum when used in a servlet environment.

## Collecting all needed libraries ##

The distribution contains a directory named `lib`; this directory contains the Aluminum modules and their dependencies. For an overview of the modules and their dependencies, see [this page](AluminumModulesAndTheirDependencies.md). For this example, you'll need the _core_, _core-library_, _xml-parser_, and _servlet_ modules with their dependencies.

Aluminum uses [SLF4J](http://slf4j.org/) for its logging, which means that you'll need an SLF4J implementation as well. This can be an adapter for [Apache log4j](http://logging.apache.org/log4j/) or [JDK logging](http://java.sun.com/javase/6/docs/api/java/util/logging/package-summary.html) if you're using one of those, or a standalone implementation such as [Logback](http://logback.qos.ch/). For this guide, the easiest thing to do is using the Logback JAR files from the `lib` directory.

## Writing a template ##

Create a file named `hello.xml` and add the following lines:

```
<c:template xmlns:c="http://aluminumproject.googlecode.com/core">
    Hello!
</c:template>
```

## Adding the Aluminum servlet to the deployment descriptor ##

Add the following lines to `web.xml`:

```
<servlet>
    <servlet-name>aluminum servlet</servlet-name>
    <servlet-class>com.googlecode.aluminumproject.servlet.AluminumServlet</servlet-class>
</servlet>

<servlet-mapping>
    <servlet-name>aluminum servlet</servlet-name>
    <url-pattern>/aluminum/*</url-pattern>
</servlet-mapping>
```

## Processing the template ##

Now you can deploy your application. Make sure that `WEB-INF/lib` contains all required libraries and that the template is placed in `WEB-INF/classes`. When your servlet container is running on port 8080 and your web application has context path `/example`, navigating a browser to http://localhost:8080/example/aluminum/hello.xml should result in the following output:

> `Hello!`

## Reading more ##

Of course, this example only scratched the surface of what's possible with Aluminum. For more information, please see [the complete documentation](Documentation.md).