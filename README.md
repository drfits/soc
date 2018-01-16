# Sling Open CMS - SOC

License: [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Master branch: [![Build Status](https://travis-ci.org/drfits/soc.svg?branch=master)](https://travis-ci.org/drfits/soc) | [![Coverage Status](https://coveralls.io/repos/github/drfits/soc/badge.svg?branch=master)](https://coveralls.io/github/drfits/soc?branch=master)

Develop branch: [![Build Status](https://travis-ci.org/drfits/soc.svg?branch=develop)](https://travis-ci.org/drfits/soc) | [![Coverage Status](https://coveralls.io/repos/github/drfits/soc/badge.svg?branch=develop)](https://coveralls.io/github/drfits/soc?branch=develop)

This project is attempt to create CMS for Java developers. Core principles are:

1. flexible for customers and reliable for production
2. simple for use and reuse
3. minimal learning curve
4. extendable with plugins

## Prerequisites

### Tools and applications for development

1. [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html)
2. [Apache Maven 3.5.0 or above](https://maven.apache.org/download.cgi)

### Code style

Code style is default for Intelligence IDEA and stored within ```config/soc.xml```. To import it please read the [documentation](https://www.jetbrains.com/help/idea/code-style.html).

### Development

Maven profile for development - ```soc-modules-development```.

## Build and run

1. To create CMS installer:
    ```cmd
    mvn clean install -Pcreate-installation-jar
    ```
2. To compile all available modules:
    ```cmd
    mvn clean install -Psoc-modules
    ```
3. To deploy all available modules on SOC instance (by default on http://localhost:8080):
    ```cmd
    mvn clean install -Psoc-modules,autoInstallBundle
    ```

