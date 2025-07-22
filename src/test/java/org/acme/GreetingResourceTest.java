package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class GreetingResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("Hello from RESTEasy Reactive"));
    }

    @Test
    void testRegisterEndpointAuthorized() {
        // Use the same secret key as in GreetingService
        String SECRET_KEY = "your-256-bit-secret";
        String jwt = io.jsonwebtoken.Jwts.builder()
            .setSubject("Shreeram")
            .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
            .compact();
        given()
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .body("{\"name\":\"Shreeram\",\"age\":30}")
        .when()
            .post("/hello/register")
        .then()
            .statusCode(200)
            .body(is("Hello Shreeram, age 30!"));
    }

    @Test
    void testRegisterEndpointUnauthorized() {
        given()
            .contentType("application/json")
            .body("{\"name\":\"Shreeram\",\"age\":30}")
        .when()
            .post("/hello/register")
        .then()
            .statusCode(401)
            .body(is("Missing or invalid Authorization header"));
    }

}