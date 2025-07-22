package org.acme.cucumbertests.common;

import io.cucumber.java.en.Then;
import io.quarkus.test.junit.QuarkusTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import io.restassured.response.Response;

@QuarkusTest
public class CommonApiSteps {

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int status) {
        assertThat(TestContext.getInstance().getResponse().getStatusCode(), is(status));
    }

    @Then("the response body should be {string}")
    public void the_response_body_should_be(String expectedBody) {
        assertThat(TestContext.getInstance().getResponse().getBody().asString(), is(expectedBody));
    }

    // No need for a setter; response is managed by TestContext
}
