WSDL2Apex
=========

WSDL2Apex takes a WSDL file and generates the Apex stubs for it. This is used in, and is part of the Force.com IDE plug-in. However, since it can be used a standalone tool, we have factored it out into its own project.

How to Use
==========

1. Clone this repository.
1. In the directory, use the command `mvn install package`.
1. After it has finished, use the command `cd target` to switch to the right directory
1. Execute the jar: `java -jar WSDL2Apex-1.0.jar [path-to-wsdl] [path-to-new-file] [want asynchronous class? (yes|no)]`
1. To clean, run `mvn clean`

To run the test, use the command `mvn test` in the base directory 

Note
====

WSDL2Apex code is now separated into the parsing part and the generated part.
If you are using the parsing and generating functions in the main class, be
sure to use the parse method first and then the generate method.

Parse method takes [path-to-wsdl]

Generate method takes in [names-of-generated-classes] [want asynchronous class? (yes|no) ] [(optional) path-to-new-file]

License
=======

[Eclipse Public License (EPL) v1.0](http://www.eclipse.org/legal/epl-v10.html)

WSDL2Apex also uses the following libraries/frameworks in their binary form, which have their own respective licenses:

* [Antlr](http://www.antlr.org/) (BSD License)
* [Google Guava](https://code.google.com/p/guava-libraries/) (Apache License)
* [Force-WSC](https://github.com/forcedotcom/wsc) (BSD License)
