package org.acme.cucumbertests.common;

import io.restassured.response.Response;

public class TestContext {
    private static final TestContext INSTANCE = new TestContext();
    private Response response;

    private TestContext() {}

    public static TestContext getInstance() {
        return INSTANCE;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
