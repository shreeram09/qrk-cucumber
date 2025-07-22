Feature: Hello API

  Scenario: GET /hello returns default greeting
    When I send a GET request to "/hello"
    Then the response status should be 200
    And the response body should be "Hello from RESTEasy Reactive"
