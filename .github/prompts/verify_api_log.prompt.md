Execute and Validate Quarkus Development Environment

Prerequisites:
- Maven installed
- Quarkus application codebase available
- Port 8080 available

Steps:

1. Verify Development Mode Status
   ```bash
   ps aux | grep quarkus:dev
   ls target/quarkus-dev.log
   ```
   - If Quarkus dev mode is not running or the log file does not exist, proceed to step 2.

2. Launch Development Server
   Skip if already running and log file exists.
   ```bash
   ./mvnw quarkus:dev | tee target/quarkus-dev.log
   ```
   - If port 8080 is in use, print a message and suggest stopping the conflicting process or changing the port.
   - If the maven wrapper is not executable, print a message and suggest setting execute permissions.

3. Application Startup
   - Wait up to 180 seconds for complete initialization.
   - Verify console or log file shows "Listening on: http://localhost:8080".
   - If the application does not start within 180 seconds, print a timeout error and suggest checking for performance issues.
   - If startup fails, search `target/quarkus-dev.log` for "ERROR", "Exception", or "Failed" and display relevant lines. Suggest checking dependencies, port conflicts, or permissions.

4. Test Endpoint (In new terminal)
   ```bash
   curl -v http://localhost:8080/hello
   ```
   - If the endpoint returns a non-200 status, print the status and response body for debugging.

5. Validation Criteria
   - Response body must match exactly: "Hello from RESTEasy Reactive"
   - File `target/quarkus-dev.log` must contain server logs
   - Server logs must include: "Received a request to /hello endpoint"
   - If the response body does not match, print the actual response and suggest checking the implementation.
   - If the log file is missing or not writable, print a warning and suggest checking permissions or disk space.
   - If the expected log entry is missing, print a message and suggest verifying logger configuration and log level.

Success Criteria:
- Endpoint returns 200 OK status
- Response matches expected string
- Log entries confirm request handling

Troubleshooting:
- If port 8080 is in use, check for conflicting processes
- Ensure maven wrapper has execute permissions
- Verify log file is writable in target directory
- If errors or exceptions are found in the log, review and resolve them before proceeding