# This workspace belongs to Memos REST Endpoints.


## Latest Deployed endpoint.
http://test-example-memo.elasticbeanstalk.com/

## Stack
 1. Maven 3 (`brew install maven`)
 2. Java 8 ([download site](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html))
 3. Spring Boot (part of maven build, no install necessary)
 4. Postgres SQL (On cloud, no install necessary)

## Build Steps
 1. build and test `mvn clean install`
 2. run server `java -jar target/rest-0.0.1-SNAPSHOT.jar`
 3. Visit http://localhost:8080/api to see supported API. It has inbuilt REST client for testing the endpoints. Click on Try it Out! when done with ready with Request Params.

## Notes:
 1. This application is being built using Spring-Boot Java Stack(Inbuilt TomCat. No Installation needed).
 2. This application is being built with TDD approach. Integration tests were considered during Test Driven development.
 3. This application also covers with Unit Tests with Mocks. But due to time limit, Unit Tests are not as rich as Integration Tests.
 4. REST Endpoints supports JSON and XML content response types.



## Supported Endpoint
Its recommended to test the endpoints using http://localhost:8080/api.
For REST Based clients.

1. Get Memos (GET)   http://localhost:8080/api/data/1/memo?page=1&limit=10
2. Get Memo by Id (GET) http://localhost:8080/api/data/1/memo/1
3. Create Memo (POST) http://localhost:8080/api/data/1/memo
 ```
      Accept: application/json
      Content-Type: application/json

      {
         "title": "Some title",
         "author": "Some author",
         "text": "Some text"
      }
```
4. Update Memo (PUT) http://localhost:8080/api/data/1/memo/1
```
      Accept: application/json
      Content-Type: application/json

      {
         "id" : 1,
         "title": "Some title",
         "author": "Some author",
         "text": "Some text"
      }
```
5. Delete Memo (DELETE) http://localhost:8080/api/data/1/memo/1


## Local dev setup
 1. Install [IntelliJ Ultimate](https://www.jetbrains.com/idea/download/). IT can provide a license.
 2. Install Lombok and Sprint Boot plugins (IntelliJ > Preferences > Plugins)
 3. Install checkstyle-idea plugins (IntelliJ > Preferences > Plugins)
 4. To run/debug the app from IDE, select MemoBootApplication class, right-click and run or debug.

## Steps and Scripts for Continuous Delivery(Jenkins) Pipeline.
 1. Code Compile. `mvn clean compile`
 2. Run CheckStyle `mvn clean checkstyle:checkstyle`
 3. Run FindBugs `mvn clean compile findbugs:check`
 4. Run Unit Test `mvn clean test`
 5. Run Integration Test `mvn clean verify`
 6. Make Build `mvn clean install`


## Future Improvements.
 1. Introduce Unit Tests to DAO Layers.
 2. Introduce Embedded DB Systems like H2DB for DAO Unit Tests
 3. Introduce ORM to get Database Independance and Portability.
 4. Introduce code coverage tool like Jacoco to maintain code coverage.
 5. Add Jmeter profile to src/test/jmeter and configure the job in the pipeline.
