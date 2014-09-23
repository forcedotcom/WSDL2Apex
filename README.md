WSDL2Apex
=========

WSDL2Apex open source code

HOW TO USE
==========

1. Clone this repository
2. In the directory, use the command mvn install package
3. After it has finished, use the command cd target to switch to the right directory
4. Execute the jar: java -jar WSDL2Apex-1.0.jar [path-to-wsdl] [path-to-new-file] [want asynchronous class?]
5. To clean, run mvn clean

To run the test, use the command mvn test in the base directory 

NOTE
====

WSDL2Apex code is now separated into the parsing part and the generated part.  If using the parsing and
generating functions in the main class, be sure to use the parse method first and then the generate
method.

Parse method takes [path-to-wsdl]

Generate method takes in [names-of-generated-classes] [want asynchronous class] [(optional) path-to-new-file]
