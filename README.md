
# qrk-cucumber

## Overview
This repository demonstrates integration testing for a Quarkus-based REST API using Cucumber. It is primarily composed of HTML, Java, and Gherkin files. The project showcases behavior-driven development (BDD) practices for REST endpoints, leveraging Cucumber for scenario definitions and Quarkus for backend implementation.

## Features
- **Quarkus REST API:** Contains endpoints for greeting and user registration, with security and JSON mapping.
- **Cucumber Integration:** Uses Gherkin syntax to define test scenarios for API behavior.
- **Testing Frameworks:** Employs JUnit 5 and REST-assured for automated tests.
- **Security:** Demonstrates Bearer token authentication for POST endpoints.
- **Logging:** API requests are logged using `org.jboss.logging.Logger`.

## Tech Stack
- Quarkus (Java backend)
- Cucumber (Gherkin for BDD)
- JUnit 5, REST-assured (Testing)
- HTML (front-end assets if present)

## Getting Started

### Run in Dev Mode
```bash
./mvnw quarkus:dev
```
Access Dev UI at: [http://localhost:8080/q/dev/](http://localhost:8080/q/dev/)

### Build a JAR
```bash
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

### Run Tests
```bash
./mvnw test
```

## API Endpoints
- `GET /hello`: Returns a greeting message.
- `POST /hello/register`: Accepts `{name, age}` JSON, requires Bearer token.

## Testing
- Feature files define API scenarios in Gherkin.
- Java step definitions interact with Quarkus REST endpoints.
- See `src/main/java/org/acme` for test code examples.

## Maven Plugin Ordering for Cucumber Test Execution and Report Generation
To ensure reliable test execution and reporting, the following Maven plugins should be configured in this order, reflecting Maven’s build lifecycle:

### 1. maven-surefire-plugin
- **Purpose:** Runs unit and Cucumber tests.
- **Goal:** `test`
- **Phase:** `test`
- **Usage:** Executes your test classes (such as Cucumber runners and step definitions).

### 2. maven-failsafe-plugin (optional, for post-integration tests)
- **Purpose:** Runs integration tests typically named `*IT.java` after packaging.
- **Goals:** `integration-test`, `verify`
- **Phases:** `integration-test`, `verify`
- **Usage:** For scenarios that require the application to be packaged and possibly running.

### 3. maven-cucumber-reporting
- **Purpose:** Generates HTML reports from Cucumber JSON output.
- **Goal:** `generate`
- **Phase:** `verify`
- **Usage:** Runs after all tests complete, using the Cucumber JSON output to generate readable reports.

### Example Plugin Ordering in `pom.xml`

```xml
<build>
  <plugins>
    <!-- 1. Surefire: runs tests during 'test' phase -->
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-plugin</artifactId>
      <version>3.1.2</version>
    </plugin>

    <!-- 2. Failsafe: runs integration tests during 'integration-test' and 'verify' phases -->
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-failsafe-plugin</artifactId>
      <version>3.1.2</version>
    </plugin>

    <!-- 3. Cucumber Reporting: generates reports during 'verify' phase -->
    <plugin>
      <groupId>net.masterthought</groupId>
      <artifactId>maven-cucumber-reporting</artifactId>
      <version>5.7.0</version>
      <executions>
        <execution>
          <id>cucumber-report</id>
          <phase>verify</phase>
          <goals>
            <goal>generate</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```

### Summary
- Surefire runs first (test phase), followed by Failsafe if configured (integration-test/verify), and finally, the Cucumber reporting plugin generates test reports during the verify phase after all tests are complete.

## Contributing
Issues and pull requests are welcome. Please ensure new features include corresponding Cucumber scenarios.

This project uses Quarkus, the Supersonic Subatomic Java Framework.  
If you want to learn more about Quarkus, please visit its website: [https://quarkus.io/](https://quarkus.io/).

## Running the application in dev mode
You can run your application in dev mode that enables live coding using:

```bash
./mvnw compile quarkus:dev
```

> **_NOTE:_** Quarkus now ships with a Dev UI, which is available in dev mode only at [http://localhost:8080/q/dev/](http://localhost:8080/q/dev/).

## Packaging and running the application
The application can be packaged using:

```bash
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.  
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

If you want to build an _über-jar_, execute the following command:

```bash
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using:

```bash
java -jar target/*-runner.jar
```

## Creating a native executable
You can create a native executable using:

```bash
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```bash
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with:

```bash
./target/quarkus-demo-1.0.0-SNAPSHOT-runner
```

If you want to learn more about building native executables, please consult: [https://quarkus.io/guides/maven-tooling](https://quarkus.io/guides/maven-tooling).

## Provided Code

### RESTEasy Reactive
Easily start your Reactive RESTful Web Services.  
[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
