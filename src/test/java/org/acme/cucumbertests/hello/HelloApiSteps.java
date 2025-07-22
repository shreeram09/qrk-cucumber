package org.acme.cucumbertests.hello;

import io.cucumber.java.en.*;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class HelloApiSteps {
    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String path) {
        org.acme.cucumbertests.common.TestContext.getInstance().setResponse(RestAssured.get(path));
    }
}
