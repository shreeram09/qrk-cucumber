# Quarkus Demo: AI Coding Agent Instructions

### Project Overview

* Framework: [Quarkus](pom.xml) (see `pom.xml` for dependencies)
* Main REST API: [`GreetingResource.java`](../src/main/java/org/acme/GreetingResource.java)
 - `/hello` (GET): Returns `"Hello from RESTEasy Reactive"`, logs each request, and simulates latency with Thread.sleep.
 - `/hello/register` (POST): Accepts JSON `{name, age}` and requires a Bearer token. Responds with a personalized greeting or 401 if the token is missing/invalid.
 - Uses `@JsonbProperty` for JSON mapping (enabled by quarkus-resteasy-jsonb).

### Build, Run, and Debug
* Dev Mode (hot reload, Dev UI):
```bash
./mvnw quarkus:dev
```
 - Dev UI: [http://localhost:8080/q/dev/](http://localhost:8080/q/dev/)
* Build JAR:
```bash
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```
* Native Build:
```
./mvnw package -Dnative
./target/quarkus-demo-1.0.0-SNAPSHOT-runner
```
* Debugging: Attach to port 5005 (see [`launch.json`](../../.vscode/launch.json)).

### Testing
* Unit/Integration Tests: Located in [`src/main/java/org/acme`](../src/main/java/org/acme)
  - Uses JUnit 5 and REST-assured (see [`GreetingResourceTest.java`](../src/test/java/org/acme/GreetingResourceTest.java)).
  - Run with:
```bash
./mvnw test
```
### Logging & Conventions
* Logging: Use org.jboss.logging.Logger (see [`GreetingResource.java`](../src/main/java/org/acme/GreetingResource.java)).
* JSON Mapping: Use @JsonbProperty for request/response DTOs.
* Auth: POST endpoints expect Authorization: Bearer ... header.

# API Timing Analysis

### GET Request with Query Parameter
```bash
curl -o /dev/null -s -w "%{http_code} %{time_namelookup} %{time_connect} %{time_appconnect} %{time_starttransfer} %{time_total}\n" "http://localhost:8080/hello?name=shreeram" | \
awk '{ printf "Status: %s\nDNS: %.0f ms\nConnect: %.0f ms\nTLS: %.0f ms\nTTFB: %.0f ms\nTotal: %.0f ms\n", $1, $2*1000, $3*1000, $4*1000, $5*1000, $6*1000 }'
```
- Ensure the URL uses `?` for query parameters, not `&` directly after the path.
- Output shows HTTP status, DNS, Connect, TLS, TTFB, and Total times in milliseconds.
- If the endpoint is unreachable or times out, check server status and network connectivity.

### POST Request with Headers and Payload
```bash
curl -X POST https://example.com/api \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-token-here>" \
  -d '{"name":"Shreeram","age":30}' \
  -o /dev/null -s \
  -w "%{http_code} %{time_namelookup} %{time_connect} %{time_appconnect} %{time_starttransfer} %{time_total}\n" | \
awk '{ printf "Status: %s\nDNS: %.0f ms\nConnect: %.0f ms\nTLS: %.0f ms\nTTFB: %.0f ms\nTotal: %.0f ms\n", $1, $2*1000, $3*1000, $4*1000, $5*1000, $6*1000 }'
```
- Replace `<your-token-here>` with a valid token if authentication is required.
- If the response is not as expected, check the endpoint, headers, and payload format.
- For HTTPS endpoints, ensure certificates are valid or use `-k` to ignore certificate errors (not recommended for production).

### Troubleshooting
- If any timing value is missing or zero, verify the endpoint is accessible and the server is running.
- For Windows, ensure `awk` is available (install via Git Bash or GnuWin32 if needed).
- If you see errors like `Could not resolve host`, check your network and DNS settings.
- For more detailed timing breakdown, add `-v` to `curl` for verbose output.

## Key Files & Patterns
* [`GreetingResource.java`](../src/main/java/org/acme/GreetingResource.java): Main REST endpoints, logging, DTOs.
* [`pom.xml`](../../pom.xml): Quarkus dependencies (quarkus-resteasy-reactive, quarkus-resteasy-jsonb, etc.).
* [`launch.json`](../../.vscode/launch.json): Debug configs for VS Code.
* [`README.md`](../../README.md): Build/run instructions and Quarkus links

## Maven pom.xml Refactoring Best Practices

When editing or refactoring pom.xml files, always follow these official Maven guidelines:

- Always start with `<?xml version="1.0" encoding="UTF-8"?>` at the top of the file.
- The `<project>` tag must be the root element and the first tag after the XML declaration.
- Place `<modelVersion>`, `<groupId>`, `<artifactId>`, `<version>`, and `<properties>` in the correct order.
- All plugins must be inside the `<build><plugins>` section.
- No duplicate or misplaced XML headers or tags.
- Follow official Maven documentation for structure and formatting.

These rules ensure Maven compatibility, editor support, and maintainable builds. Always validate the pom.xml after changes.

## File Move Best Practice

If a cut operation fails when moving content within a file, use the following fallback procedure:
1. Copy the desired content.
2. Paste it at the target location.
3. Remove the original content from its previous location.

This ensures reliable file moves and preserves all instructions as intended.