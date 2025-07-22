# qrk-cucumber

## Contributing
Issues and pull requests are welcome. Please ensure new features include corresponding Cucumber scenarios.

This project uses Quarkus, the Supersonic Subatomic Java Framework.  
If you want to learn more about Quarkus, please visit its website: [https://quarkus.io/](https://quarkus.io/).

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

## Maven Plugin Configuration for Cucumber Test Execution and Report Generation

### Understanding Surefire vs Failsafe
The key to successful Cucumber testing in Quarkus is understanding which Maven plugin handles which tests:

- **Surefire Plugin**: Runs unit tests during `test` phase, excludes `**/*IT.java` files
- **Failsafe Plugin**: Runs integration tests during `integration-test` phase, includes `**/*IT.java` files
- **Critical Insight**: For `mvn install`, only Failsafe plugin configuration matters for IT test report generation

### Essential Dependencies
```xml
<properties>
    <cucumber.version>7.18.0</cucumber.version>
    <quarkus-cucumber.version>1.3.0</quarkus-cucumber.version>
    <cucumber-reporting.version>5.9.0</cucumber-reporting.version>
</properties>

<dependencies>
    <!-- Cucumber Core -->
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-java</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- Quarkus Cucumber Integration -->
    <dependency>
        <groupId>io.quarkiverse.cucumber</groupId>
        <artifactId>quarkus-cucumber</artifactId>
        <version>${quarkus-cucumber.version}</version>
        <scope>test</scope>
    </dependency>
    
    <!-- JUnit Platform Support -->
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-junit-platform-engine</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Optimal Plugin Configuration

#### 1. maven-surefire-plugin (Unit Tests)
```xml
<plugin>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>${surefire-plugin.version}</version>
    <configuration>
        <properties>
            <configurationParameters>
                cucumber.junit-platform.naming-strategy=long
            </configurationParameters>
        </properties>
        <systemPropertyVariables>
            <java.util.logging.manager>org.jboss.logmanager.LogManager</java.util.logging.manager>
            <maven.home>${maven.home}</maven.home>
            <!-- NO cucumber.plugin needed here for mvn install -->
        </systemPropertyVariables>
        <excludes>
            <exclude>**/*IT.java</exclude>
        </excludes>
    </configuration>
</plugin>
```

#### 2. maven-failsafe-plugin (Integration Tests) - **Critical for Report Generation**
```xml
<plugin>
    <artifactId>maven-failsafe-plugin</artifactId>
    <version>${surefire-plugin.version}</version>
    <executions>
        <execution>
            <goals>
                <goal>integration-test</goal>
                <goal>verify</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <systemPropertyVariables>
            <java.util.logging.manager>org.jboss.logmanager.LogManager</java.util.logging.manager>
            <maven.home>${maven.home}</maven.home>
            <!-- ESSENTIAL for report generation during mvn install -->
            <cucumber.plugin>pretty, html:target/cucumber-reports/cucumber.html, json:target/cucumber-reports/cucumber.json, junit:target/cucumber-reports/cucumber.xml</cucumber.plugin>
            <cucumber.glue>org.acme.cucumbertests</cucumber.glue>
        </systemPropertyVariables>
        <includes>
            <include>**/*IT.java</include>
        </includes>
    </configuration>
</plugin>
```

#### 3. maven-cucumber-reporting (Enhanced Reports - Optional)
```xml
<plugin>
    <groupId>net.masterthought</groupId>
    <artifactId>maven-cucumber-reporting</artifactId>
    <version>${cucumber-reporting.version}</version>
    <executions>
        <execution>
            <id>execution</id>
            <phase>verify</phase>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <projectName>${project.artifactId}</projectName>
                <outputDirectory>${project.build.directory}/cucumber-plugin-reports</outputDirectory>
                <inputDirectory>${project.build.directory}/cucumber-reports</inputDirectory>
                <jsonFiles>
                    <param>**/*.json</param>
                </jsonFiles>
                <checkBuildResult>false</checkBuildResult>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### Clean Cucumber Test Classes
With proper Maven configuration, your Cucumber IT classes become minimal:

```java
@QuarkusTest
public class HelloApiCucumberIT extends CucumberQuarkusTest {
    // No additional configuration needed - all centralized in Maven plugins
}
```

### Integration Test Execution
Enable integration tests by default:
```xml
<properties>
    <skipITs>false</skipITs>  <!-- Changed from true to false -->
</properties>
```

### Maven Commands
```bash
# Run all tests including integration tests
mvn clean install

# Run only unit tests (excludes IT)
mvn clean test

# Run only integration tests
mvn clean verify
```

### Generated Reports
```
target/cucumber-reports/
├── cucumber.html     # Basic HTML report
├── cucumber.json     # JSON data for advanced reporting
└── cucumber.xml      # JUnit XML format

target/cucumber-plugin-reports/  # Enhanced reports (if using maven-cucumber-reporting)
```

### Maven Lifecycle Flow
```
mvn install execution:
├── test phase (Surefire) → Unit tests only
├── package phase → Create JAR
├── integration-test phase (Failsafe) → IT tests + Report generation
└── verify phase → Validate results + Enhanced reporting
```

### Key Success Factors
1. **Correct Plugin Configuration**: Failsafe plugin must have Cucumber properties for `mvn install`
2. **No Redundant Annotations**: Avoid `@ConfigurationParameter` in test classes
3. **Proper Test Naming**: Use `*IT.java` suffix for integration tests
4. **Centralized Configuration**: All Cucumber settings in Maven plugins, not individual classes

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
