Most templates generate output. This output can be quite diverse - possibilities include an e-mail, a log file, a single HTML page or even a complete website. To allow a template to produce output, it is given access to a writer. During the processing of a template, the writer can be replaced; this means that it is possible to use various writers within a single template.

## Operations on a writer ##

The [Writer](http://docs.aluminumproject.googlecode.com/hg/api/com/googlecode/aluminumproject/writers/Writer.html) interface has four methods:

|Method|Description|
|:-----|:----------|
|`write(Object)`|Writes an object.|
|`flush()`|Makes sure that all objects that were written but buffered are removed from the buffer and actually written.|
|`clear()`|Removes all buffered objects that were written since the last time the writer was flushed.|
|`close()`|Flushes and closes the writer.|

The `write` method writes an object. Note that this object does not necessarily have to be text. Writers are allowed to ignore or modify written objects.

Some writers do not write their objects directly, but save them in a buffer. The `flush` and `clear` methods operate on this buffer: flushing a writer forces the buffered objects to be written, clearing a writer discards all buffered objects.

The `close` method flushes the writer and closes it. After a writer has been closed, it shouldn't be used anymore. The initial writer will be closed by the template engine.

## Decorative writers ##

A writer may wrap another writer, e.g. to filter written objects. Such a writer is called a decorative writer. The [DecorativeWriter](http://docs.aluminumproject.googlecode.com/hg/api/com/googlecode/aluminumproject/writers/DecorativeWriter.html) interface adds two methods to its parent interface `Writer`: `getWriter` and `setWriter`. Flushing a decorative writer causes the underlying writer to be flushed as well; similarly, closing a decorative writer closes its underlying writer too.

## An overview of available writers ##

Aluminum ships with the following writers:

|Name|Module|Description|
|:---|:-----|:----------|
|[StringWriter](http://docs.aluminumproject.googlecode.com/hg/api/com/googlecode/aluminumproject/writers/StringWriter.html)|`core`|Concatenates written text. Note that non-text is not written.|
|[TextWriter](http://docs.aluminumproject.googlecode.com/hg/api/com/googlecode/aluminumproject/writers/TextWriter.html)|`core`|Converts all written objects to text. It can be useful to wrap a string writer in a text writer.|
|[OutputStreamWriter](http://docs.aluminumproject.googlecode.com/hg/api/com/googlecode/aluminumproject/writers/OutputStreamWriter.html)|`core`|Writes objects to an output stream.|
|[ListWriter](http://docs.aluminumproject.googlecode.com/hg/api/com/googlecode/aluminumproject/writers/ListWriter.html)|`core`|Adds all written objects to a list.|
|[NullWriter](http://docs.aluminumproject.googlecode.com/hg/api/com/googlecode/aluminumproject/writers/NullWriter.html)|`core`|Ignores all written objects.|
|[FileWriter](http://docs.aluminumproject.googlecode.com/hg/api/com/googlecode/aluminumproject/writers/FileWriter.html)|`io-library`|Writes objects to a file.|
|[ResponseWriter](http://docs.aluminumproject.googlecode.com/hg/api/com/googlecode/aluminumproject/servlet/writers/ResponseWriter.html)|`servlet`|Writes objects to a servlet response.|

The following decorative writers are available as well:

|Name|Module|Description|
|:---|:-----|:----------|
|[ToggleableWriter](http://docs.aluminumproject.googlecode.com/hg/api/com/googlecode/aluminumproject/writers/ToggleableWriter.html)|`core`|Writes or ignores written objects. Its behaviour can be toggled.|
|[PreserveWhitespaceWriter](http://docs.aluminumproject.googlecode.com/hg/api/com/googlecode/aluminumproject/writers/PreserveWhitespaceWriter.html)|`text-library`|Collapses whitespace of a certain type in written text: e.g. keeps only a single space between words or keeps at most two newlines between paragraphs.|
|[TrimWriter](http://docs.aluminumproject.googlecode.com/hg/api/com/googlecode/aluminumproject/writers/TrimWriter.html)|`text-library`|Removes leading and/or trailing whitespace from written text.|