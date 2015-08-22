A way to write Aluminum templates is with AluScript. AluScript templates are parsed by the AluScript parser. This page explains the structure of an AluScript template, tells a bit about the AluScript syntax, and shows the configuration possibilities of the AluScript parser.

## The structure of an AluScript template ##

AluScript templates are parsed line by line. Each line represents a comment, an instruction, or text. Empty lines are ignored.

### Comments ###

A comment starts with a `#` symbol:

```
# This is a comment.

This will be printed.
```

### Instructions ###

An instruction starts with an `@` symbol:

```
@library (c: http://aluminumproject.googlecode.com/core)

@c.each (elements: ${templates}, element name: template)
	@c.include (name: ${template}, c.if: ${not empty template})
```

This template demonstrates a couple of things:

  * If the name of an instruction contains a dot, then the instruction represents an action, if it doesn't, it represents a built-in instruction;
  * Instructions can have parameters, parameters are separated with commas;
  * If the name of a parameter contains a dot, it is interpreted as an action contribution;
  * Parent/child relations are expressed through indentation;
  * By default, template names are used as-is.

There are a number of built-in instructions:

|Instruction|Description|
|:----------|:----------|
|`@library` |Declares a library, so that actions, action contributions, and functions in the library can be used in subsequent lines at the same nesting level.|
|`@newline` |Inserts a newline character.|
|`@tab`     |Inserts a tab character.|

The tab character is used to indent lines. Indentation is only necessary to express a parent/child relation between an action (the parent) and its body (the children). Note that the AluScript parser is very strict about indentation: only a single tab is accepted as indentation (spaces are not, neither are multiple tabs).

### Text ###

Any line that is not recognised as a comment or as an instruction is treated as text:

```
Hello, ${name}!
```

As this template illustrates, text can contain expressions. By default, the AluScript parser adds a newline character after each line of text.

## Escape characters ##

To avoid characters to be recognised as special characters, they can be escaped. It's possible to escape single characters as well as multiple characters.

Single characters are escaped by prefixing them with a backslash:

```
@library (c: http://aluminumproject.googlecode.com/core)

@c.write (value: \:\))
```

Multiple characters can be escaped with either single or double quotes:

```
@library (c: http://aluminumproject.googlecode.com/core)

@c.write (value: ':)')
@newline
@c.write (value: ":)")
```

It is not disallowed to escape text that does not need escaping, so the following library declaration is accepted as well:

```
@library (c: 'http://aluminumproject.googlecode.com/core')
```

## Configuration ##

The AluScript parser supports the following [configuration](Configuration.md) parameters:

|Parameter|Description|
|:--------|:----------|
|`parser.aluscript.template_extension`|The extension of AluScript templates (not used by default). If all templates have the same extension (such as `alu`), configuring a template extension saves you from having to supply it all the time.|
|`parser.aluscript.template_name_translator.class`|The FQCN of the [template name translator](http://docs.aluminumproject.googlecode.com/hg/api/com/googlecode/aluminumproject/parsers/TemplateNameTranslator.html) that should be used.|
|`parser.aluscript.automatic_newlines`|Whether newlines are inserted after text lines or not (`true` by default).|