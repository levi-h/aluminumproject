Aluminum is a template engine: it processes templates. These templates can do many different things: create documents, produce object models, run programs... the possibilities are endless.

A template engine is created with a certain [configuration](Configuration.md). A configuration is a read-only resource that contains various parts of the template engine, such as a registry for [converters](Converters.md), all of the [libraries](Libraries.md) that can be used, and all available [parsers](Parsers.md). All of these parts are replaceable, though a [default configuration](http://docs.aluminumproject.googlecode.com/hg/api/com/googlecode/aluminumproject/configuration/DefaultConfiguration.html) exists with sensible defaults.

After a template engine has been created, it can be run many times. For each template that it processes, Aluminum needs a template name, a parser name, a [context](Contexts.md), and a [writer](Writers.md). With this input, the following steps are taken:

  * The supplied template name is being transformed into a template:
    * If a [cache](http://docs.aluminumproject.googlecode.com/hg/api/com/googlecode/aluminumproject/cache/Cache.html) is configured and it contains a template with the requested name, that template is being used;
    * If no cache is configured or if it does not yet contain a template with the given name, one of the parsers in the configuration (namely, the one with the provided parser name) is being used to parse the template;
  * The template is being processed: the context and the writer are handed to its top-level template elements (which may in turn decide to pass them to their child elements, et cetera);
  * The writer is being closed.

The template engine does not have any output; instead, a template can change its context and write objects to the writer when it is being processed.