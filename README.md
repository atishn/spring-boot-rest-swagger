# This workspace belongs to Memos REST SERVICES.

## Stack
 1. Maven 3 (`brew install maven`)
 2. Java 8 ([download site](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html))
 3. Spring Boot (part of maven build, no install necessary)
 4. Postgres SQL (On cloud, no install necessary)

## Build Steps
 1. build and test `mvn clean install`
 2. run server `java -jar .build/rest-0.0.1-SNAPSHOT.jar `
 3. Visit http://localhost:8080/api to see supported API. It has inbuilt REST client for testing the endpoints. Click on Try it Out! when done with ready with Request Params.

## Local dev setup
 1. Install [IntelliJ Ultimate](https://www.jetbrains.com/idea/download/). IT can provide a license.
 2. Install Lombok and Sprint Boot plugins (IntelliJ > Preferences > Plugins)
 3. Install checkstyle-idea plugins (IntelliJ > Preferences > Plugins)
 4. To run/debug the app from IDE, select TRUBootApplication class, right-click and run or debug.

## Steps and Scripts for Continuous Delivery(Jenkins) Pipeline.
 1. Code Compile. `mvn clean compile`
 2. Run CheckStyle `mvn clean checkstyle:checkstyle`
 3. Run FindBugs `mvn clean compile findbugs:check`
 4. Run Unit Test `mvn clean test`
 5. Run Integration Test `mvn clean verify`
 6. Make Build `mvn clean install`


## Notes:
 1. This application is being built using Spring-Boot with Java Stack.
 2. This appilcation is being built with TDD approach. Integration tests were considered during Test Driven development.
 3. This application also covers with Unit Tests with Mocks. But due to time limit, Unit Tests are not as rich as Integration Tests.

## Future Improvements.
 1. Introduce Unit Tests to DAO Layers.
 2. Introduce Embedded DB Systems like H2DB for DAO Unit Tests
 3. Introduce ORM to get Database Independance and Portablity.