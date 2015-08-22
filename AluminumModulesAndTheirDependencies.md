This page lists all of Aluminum's modules and their dependencies. Note that although each library dependency is a link to the framework's homepage, you don't have to download the dependencies by hand - the distribution archive contains all necessary JAR files.

|Module|Description|Depends on modules|Depends on libraries|Depends on Java version|
|:-----|:----------|:-----------------|:-------------------|:----------------------|
|`aludoc`|Generates documentation.|`core`<br><code>g11n-library</code><table><thead><th>-                   </th><th>1.5                    </th></thead><tbody>
<tr><td><code>aluscript-parser</code></td><td>A parser that reads AluScript templates.</td><td><code>core</code> </td><td>-                   </td><td>1.5                    </td></tr>
<tr><td><code>cli</code></td><td>The command-line interface.</td><td><code>core</code> </td><td><a href='http://jopt-simple.sourceforge.net/'>JOpt Simple</a><br><a href='http://juel.sourceforge.net/'>JUEL</a><br><a href='http://logback.qos.ch/'>Logback</a></td><td>1.5                    </td></tr>
<tr><td><code>beans-library</code></td><td>A library that works with JavaBeans.</td><td><code>core</code> </td><td>-                   </td><td>1.5                    </td></tr>
<tr><td><code>core</code></td><td>The template engine.</td><td>-                 </td><td><a href='http://www.slf4j.org/'>SLF4J</a></td><td>1.5                    </td></tr>
<tr><td><code>core-library</code></td><td>A library with actions and functions that almost every template needs.</td><td><code>core</code> </td><td>-                   </td><td>1.5                    </td></tr>
<tr><td><code>ds-library</code></td><td>A library that creates and operates on data structures.</td><td><code>core</code> </td><td>-                   </td><td>1.5                    </td></tr>
<tr><td><code>el-expressions</code></td><td>An expression factory that uses Unified EL.</td><td><code>core</code> </td><td><a href='http://java.sun.com/javaee/5/docs/api/javax/el/package-summary.html'>EL API</a><br><a href='http://www.csg.is.titech.ac.jp/~chiba/javassist/'>Javassist</a></td><td>1.5                    </td></tr>
<tr><td><code>g11n-library</code></td><td>A library that helps with the globalisation of templates.</td><td><code>core</code> </td><td>-                   </td><td>1.5                    </td></tr>
<tr><td><code>html-library</code></td><td>A library that generates HTML documents.</td><td><code>core</code> </td><td>-                   </td><td>1.5                    </td></tr>
<tr><td><code>io-library</code></td><td>A library that enables templates to use I/O.</td><td><code>core</code> </td><td>-                   </td><td>1.5                    </td></tr>
<tr><td><code>logging-library</code></td><td>A library that facilitates logging from within templates.</td><td><code>core</code> </td><td>-                   </td><td>1.5                    </td></tr>
<tr><td><code>mail-library</code></td><td>A library that allows template authors to send e-mails.</td><td><code>core</code> </td><td><a href='http://java.sun.com/javaee/5/docs/api/javax/mail/package-summary.html'>JavaMail API</a></td><td>1.5                    </td></tr>
<tr><td><code>scripting-library</code></td><td>A library that enables scripting inside templates.</td><td><code>core</code> </td><td>-                   </td><td>1.6                    </td></tr>
<tr><td><code>servlet</code></td><td>The Aluminum servlet.</td><td><code>core</code> </td><td><a href='http://java.sun.com/products/servlet/2.5/docs/servlet-2_5-mr2/index.html'>Servlet API</a></td><td>1.5                    </td></tr>
<tr><td><code>text-library</code></td><td>A library that contains text-related features.</td><td><code>core</code> </td><td>-                   </td><td>1.5                    </td></tr>
<tr><td><code>xml-library</code></td><td>A library for generating and processing XML documents.</td><td><code>core</code> </td><td><a href='http://xom.nu/'>XOM</a></td><td>1.5                    </td></tr>
<tr><td><code>xml-parser</code></td><td>A parser that reads XML templates.</td><td><code>core</code> </td><td>-                   </td><td>1.5                    </td></tr>
<tr><td><code>xml-serialiser</code></td><td>A serialiser that writes templates to XML.</td><td><code>core</code> </td><td>-                   </td><td>1.5                    </td></tr>