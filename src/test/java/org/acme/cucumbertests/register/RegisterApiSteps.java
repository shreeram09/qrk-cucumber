package org.acme.cucumbertests.register;

import io.cucumber.java.en.*;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import java.util.Map;
import java.util.HashMap;

@QuarkusTest
public class RegisterApiSteps {
    private String jwtToken;
    private Map<String, Object> requestBody = new HashMap<>();
    private String authHeader;

    @Given("I have a valid JWT token for {string}")
    public void i_have_a_valid_jwt_token_for(String name) {
        String SECRET_KEY = "your-256-bit-secret";
        jwtToken = io.jsonwebtoken.Jwts.builder()
            .setSubject(name)
            .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
            .compact();
    }

    @When("I send a POST request to {string} with body:")
    public void i_send_a_post_request_to_with_body(String path, io.cucumber.datatable.DataTable dataTable) {
        requestBody.clear();
        Map<String, String> map = dataTable.asMap(String.class, String.class);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if ("age".equals(entry.getKey())) {
                try {
                    requestBody.put("age", Integer.parseInt(entry.getValue()));
                } catch (NumberFormatException e) {
                    requestBody.put("age", entry.getValue());
                }
            } else {
                requestBody.put(entry.getKey(), entry.getValue());
            }
        }
        Response resp;
        if (authHeader != null) {
            resp = RestAssured.given()
                .header("Authorization", authHeader)
                .contentType("application/json")
                .body(requestBody)
                .post(path);
        } else {
            resp = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .post(path);
        }
        org.acme.cucumbertests.common.TestContext.getInstance().setResponse(resp);
    }

    @And("I set the Authorization header to the valid JWT")
    public void i_set_the_authorization_header_to_the_valid_jwt() {
        authHeader = "Bearer " + jwtToken;
    }


}
