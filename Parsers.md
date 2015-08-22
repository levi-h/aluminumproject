A parser is responsible for turning a (textual) template into an object structure. The template can be a file, but it can have other origins as well. Aluminum ships with two parsers: an XML parser and an AluScript parser.

## The XML parser ##

The following code shows an example of an XML template:

```
<c:template xmlns:c="http://aluminumproject.googlecode.com/core">
    <c:set-variable name="name" value="you" c:if="${empty templateScope.name}"/>

    Hello, ${name}!
</c:template>
```

For more information about the XML parser, please visit [this page](XMLParser.md).

## The AluScript parser ##

The template above would look as follows in AluScript:

```
@library (c: http://aluminumproject.googlecode.com/core)

@c.set variable (name: name, value: you, c.if: ${empty templateScope.name})

Hello, ${name}!
```

Details about the AluScript parser can be found [here](AluScriptParser.md).

## Choosing the right parser ##

Which parser you use to write Aluminum templates in depends on a couple of things, but most of all on your personal taste. We recommend trying them both for a while and see which one you like best. You could also decide to choose a parser for each scenario (e.g. AluScript for e-mails, XML for HTML pages, and so on).

Although there is no such thing as a best parser, there are a couple of differences between the two parsers:

  * XML templates can be validated (a future version of Aluminum will ship with a tool that generates schemas for libraries), something that is not available for AluScript templates;
  * In AluScript templates a parent/child relation is expressed through indentation, XML templates need open and close tags;
  * There are more XML editors than AluScript ones;
  * All of the whitespace in XML templates will become text elements, the whitespace handling in AluScript templates is more flexible;
  * The XML parser allows tags without a namespace to be used for libraries, whereas in AluScript instructions without prefix are reserved for the parser;
  * XML documents have a single root, AluScript templates don't have this restriction.