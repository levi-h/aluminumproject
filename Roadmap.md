## Changes to existing features ##

  * ~~Converters should work with types, not classes~~
  * ~~Libraries should be categorised; all current libraries (core, I/O, text) fall into general~~
  * ~~Actions should no longer set context variables; two action contributions (name/scope) should take care of it~~
  * ~~The CLI should be configurable (through ~/.aluminum/alu.properties)~~
  * ~~When using the default action factory, setters should be optional for action parameters~~
  * ~~It should be possible to use action contributions as actions~~
  * ~~Actions should be able to require ancestor actions~~
  * There should be translators for function (argument) names <sup>0.9</sup>
  * AluScript should have a serialiser <sup>0.9</sup>
  * ~~The write action should accept a writer;~~ there should be functions to obtain writers (such as stdout/stderr <sup>0.9</sup>~~, or a file writer~~)
  * ~~The text library should ship with various functions~~
  * The I/O library should work with resources, not just with files (this will be easier with NIO2, which is part of Java 7) <sup>0.9</sup>
  * AluDoc should support links (to other library elements or Javadoc) <sup>0.9</sup>
  * It should be possible to add examples to AluDoc pages <sup>0.9</sup>
  * ~~Exceptions should contain an origin when it is available~~
  * ~~Actions should be usable as functions~~

## New features ##

### New libraries ###

  * General: ~~beans~~, ~~data structures and algorithms (ds)~~, ~~globalisation (g11n)~~, ~~scripting~~, command line <sup>0.9</sup>
  * Communication: ~~mail~~, ~~logging~~
  * File formats: ~~HTML~~, ~~XML~~

### Integration ###

  * It should be possible to configure Aluminum in Spring using a custom namespace <sup>0.9</sup>
  * There should be a Spring MVC view resolver <sup>0.9</sup>
  * It should be possible to use Aluminum templates in Grails applications <sup>(After) 1.0</sup>
  * JSP tags should be supported <sup>(After) 1.0</sup>
  * There should be some kind of test support <sup>1.0</sup>
  * Aluminum should be usable in Camel endpoints <sup>(After) 1.0</sup>

### New tools ###

  * ~~AluDoc (similar to TLDDoc)~~
  * A tool to generate schemas for libraries <sup>1.0</sup>
  * AluGen <sup>0.9</sup>

### Other tasks ###

  * ~~The project should have a blog~~
  * ~~The API documentation should contain package descriptions~~
  * More pages should be added to the WIKI <sup>1.0</sup>
  * Aluminum should be available in Maven Central <sup>1.0</sup>