# sr-microservice-skeleton
This repo is to store the project skeleton that includes all the configuration as a template and will be used for generating the microservices

# Generated project characteristics
- JPA (Hibernate/HSQLDB/Spring Data JPA)
- JUnit/Mockito
- Generic JMS publisher/handler
- Code style checker
- Dockerfile
- Logging for request/response with WebClient

## install skeleton repository
```sh
mvn -B -U clean install 
```
You can verify the result by visit: C:\Users\{your-account}\.m2\repository\com

## Generate a project based on skeleton archetype project
use this command to generate project
```sh
 mvn archetype:generate "-DarchetypeGroupId=com.nashtech" /
                           "-DarchetypeArtifactId=duccao-archetype" / 
                           "-DarchetypeVersion=0.0.1-SNAPSHOT" /
                           "-DgroupId=<project_group_id>'" /
                           "-DartifactId=<project_artifact_id>" /
                           "-Dversion=<project_version>" /
                           "-Dpackage=<project_package>" /
                           "-Ddescription=<project_description>"
```
example:
```sh
 mvn archetype:generate "-DarchetypeGroupId=com.nashtech" /
                           "-DarchetypeArtifactId=duccao-archetype" / 
                           "-DarchetypeVersion=0.0.1-SNAPSHOT" /
                           "-DgroupId=com.duccao" /
                           "-DartifactId=testing-archetype" /
                           "-Dversion=1.0-SNAPSHOT" /
                           "-Dpackage=com.duccao.learn" /
                           "-Ddescription=This is description project"
```
Confirm the values you have entered are correct:
```sh
    [INFO] Generating project in Interactive mode
    [INFO] Archetype repository not defined. Using the one from [com.nashtech:sr-microservice-skeleton-archetype:0.0.1-SNAPSHOT] found in catalog local
    [INFO] Using property: description = This is description project
    [INFO] Using property: groupId = com.nashtech
    [INFO] Using property: artifactId = subscription-service
    [INFO] Using property: version = 1.0-SNAPSHOT
    [INFO] Using property: package = com.nashtech.subscription
    Define value for property 'mainClassNamePrefix' SubscriptionService: :
    Confirm properties configuration:
    description: This is description project
    groupId: com.duccao
    artifactId: testing-archetype
    version: 1.0-SNAPSHOT
    package: com.nashtech.subscription
    mainClassNamePrefix: LearningApplication
     Y: : y
```
--note: mainClassNamePrefix is prefix name of a class, it is using for class main

review your result!

