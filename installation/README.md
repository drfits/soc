# Sling Open CMS - SOC

Project provide ability to create installation module of SOC.

## Compile

1. Build SOC application:

       mvn clean install

2. Start the generated jar with
   
       java -jar target/soc-installation-builder-1.0.0-SNAPSHOT.jar

## Documentation

For compile process configuration, customization or update please read [Apache Sling Provisioning] documentation first.

## Changes

As initial implementation Sling provisioning was taken. All changed places have to be marked like ```[SOC]:```
which allow to distinguish changes more easily.

[Apache Sling Provisioning]:https://sling.apache.org/documentation/development/slingstart.html
