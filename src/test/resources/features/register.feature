Feature: Register API

  Scenario: POST /hello/register with valid JWT
    Given I have a valid JWT token for "Shreeram"
    And I set the Authorization header to the valid JWT
    When I send a POST request to "/hello/register" with body:
      | name | Shreeram |
      | age  | 30       |
    Then the response status should be 200
    And the response body should be "Hello Shreeram, age 30!"

  Scenario: POST /hello/register without JWT
    When I send a POST request to "/hello/register" with body:
      | name | Shreeram |
      | age  | 30       |
    Then the response status should be 401
    And the response body should be "Missing or invalid Authorization header"
