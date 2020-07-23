# PrePro Language Implementation

The PrePro DSL is designed to simplify calculations with time series and
is mainly applied for pre-processing data used in machine learning applications
(hence the name). PrePro is statically and strongly typed and is devised to be
used in an interoperability environment with another language, typically Java,
which passes its data to and from the DSL.

This is a repo to which the project was pushed after its termination.
The SL fork and the commit history of the project can be viewed [here](https://github.com/LaHuBeontra/preprolanguage).

### Prerequisites

To run a program using the Graal/Truffle Framework's language interoperability, you need to:

1. Install [Maven](https://maven.apache.org/download.cgi).
2. Install GraalVM according to the instructions [here](https://www.graalvm.org/getting-started/).
3. Clone the repository and configure it as a Maven project (the standard way to run a Graal project according to the developers).
4. Set the GraalVM installation as the JDK used in your Java IDE.

### Windows

The tests can be run using `mvn test`.
Starting a Java program which reads a PrePro file without Maven was not achieved using Windows
due to illegal characters in the path when searching for the truffle-api.jar. 

### Linux

If you are running a Linux system, uncomment the modules "native" and "component" in the top-level pom.xml if you want them to be built by Maven.

The tests can be run in the same way as above, and programs which include interoperability between Java and PrePro can be started in the usual way.

### Tests

The code is tested with with the GraalVM 'graalvm-ce-java11-windows-amd64-20.1.0.zip'.
