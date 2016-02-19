# StarGenetics

## Configuring your development environment

I'm writing these instructions for an experienced developer
who isn't familiar with the Java development environment.
Hopefully I'll hit a balance between what you know and what
you don't. 

### Install Java JDK

If you have a Java JDK installed that is version 1.7.0_25 or later
you can skip this step. Otherwise, get the current Java JDK from 
[http://www.oracle.com/technetwork/java/javase/downloads/index.html]
(http://www.oracle.com/technetwork/java/javase/downloads/index.html). 
Download and install the Java Platform Standard Edition (SE) JDK.
Verify the installation by entering this command in a terminal 

```
  javac -version
```

It should return the version of ``javac`` (the Java bit-code compiler)
that you installed.  If the program isn't found or the version number 
isn't what you installed, you may have to set your ``JAVA_HOME``
environment variable. You can refer to Oracle's installation instructions
for your operating system or consult the Internet.


### Install Ant

Ant is a Java build tool similar to ``make``, but with Java specific 
support.  Ant is available here: [http://ant.apache.org/]
(http://ant.apache.org/). It is probably also available through your system's 
package manager.

### Install Python

Check to see if you have Python installed. Enter this command in a terminal

```
  python --version
```

It should return the version of the installed Python interpreter. If you have 
a minimum version of 2.6.6 you're fine. If you don't have Python installed,
it is available here: [https://www.python.org/](https://www.python.org/).

### Install wget

wget is a utility for retrieving files using Internet protocols such as HTTP,
HTTPS, and FTP.  wget is available here: [https://www.gnu.org/software/wget/]
(https://www.gnu.org/software/wget/) It is probably also available through 
your system's package manager.

## Obtaining a new application certificate

Application certificates can be obtained from [mitcert@mit.edu](mitcert@mit.edu). 
IS&T provides instructions here 
[https://wikis.mit.edu/confluence/display/devtools/How+to+acquire+and+verify+a+x509+Application+Certificate]
(https://wikis.mit.edu/confluence/display/devtools/How+to+acquire+and+verify+a+x509+Application+Certificate).

Name the certificate ``stargenetics.app.mit.edu.cer``

## Signing the jar


