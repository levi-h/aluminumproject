Templates are processed in a certain context. The outcome of a template will most likely be different when it is being processed in a different context. Contexts are dynamic: they can be changed by the template that uses them.

Aluminum ships with two contexts: a [default context](http://docs.aluminumproject.googlecode.com/hg/api/com/googlecode/aluminumproject/context/DefaultContext.html) and a [servlet context](http://docs.aluminumproject.googlecode.com/hg/api/com/googlecode/aluminumproject/servlet/context/ServletContext.html). The default context is intended for [standalone](Embedding.md) or [command line](CLI.md) use; the servlet context is used [in a servlet environment](Web.md).

A context contains scoped variables and implicit objects. Some contexts support the creation of subcontexts. The following sections describe these topics.

## Variables ##

A context can contain a number of variables that hold information. A template can base its behaviour upon this information: e.g. it might decide not to execute some actions when a certain variable holds a particular value. Of course, a template can do all kinds of other things with variables, such as [writing](Writers.md) their contents.

Each context variable has a name, a value and a scope. The name of the variable is unique within its scope, but it's possible for two variables in different scopes to have the same name. The value of a variable can have any type. As their name implies, variables can be changed after they have been created.

A context has to have at least one variable scope named _template_ (in addition, the servlet context contains the _request_, _session_, and _application_ scopes). It's possible to add scopes to a context; these scopes can later be removed from the context as well. When a scope is removed from a context, the variables in that scope are no longer available.

The following table lists the operations that can be performed on context variables:

|Operation|Syntax|
|:--------|:-----|
|Add or replace a variable|`context.setVariable("var", "value");`<br><code>context.setVariable("template", "var", "value");</code>
<tr><td>Read a variable from a scope</td><td><code>context.getVariable("template", "var");</code></td></tr>
<tr><td>Find a variable in all available scopes</td><td><code>context.findVariable("var");</code></td></tr>
<tr><td>Remove a variable</td><td><code>context.removeVariable("template", "var");</code></td></tr></tbody></table>

The same operations can be performed from inside a template as well (using the <a href='CoreLibrary.md'>core library</a> and an <a href='Expressions.md'>expression language</a>):<br>
<br>
<table><thead><th>Operation</th><th>Syntax</th></thead><tbody>
<tr><td>Add or replace a variable</td><td><code>&lt;c:set-variable name="var" value="value"/&gt;</code><br><code>&lt;c:set-variable scope="template" name="var" value="value"/&gt;</code></td></tr>
<tr><td>Read a variable from a scope</td><td><code>${templateScope.var}</code></td></tr>
<tr><td>Find a variable in all available scopes</td><td><code>${var}</code></td></tr>
<tr><td>Remove a variable</td><td><code>&lt;c:remove-variable scope="template" name="var"/&gt;</code></td></tr></tbody></table>

## Implicit objects ##

Besides variables, a context can contain a number of implicit objects as well. Implicit objects are not scoped, have a unique name, and can both be added to and removed from a context. The names of implicit objects should not start with _aluminum_: these are reserved for internal use.

We already saw an example of an implicit object: `${templateScope.var}` was used to read a variable named _var_ from the template scope. Such an implicit object is available for each scope: e.g. after a scope with the name _loop_ has been added, its variables are available via the implicit object _loopScope_. This object is a `Map` and is live, which means that changes to the implicit object are propagated through to the scope.

The servlet context has three more implicit objects: _application_, _request_, and _response_.

## Subcontexts ##

When a context allows it, it's possible to create a subcontext of it. Both the default context and the servlet context support this. To give an example of where subcontexts are used: when a template includes another template, the included template is processed in a subcontext of the including template's context.

A subcontext does not inherit its parent's implicit objects or variables. Variables that are set in the subcontext are not contributed back to the parent context. In the future, it might become possible to change this behaviour.