package org.acme;

import jakarta.json.bind.annotation.JsonbProperty;

public class UserRequest {
    @JsonbProperty("name")
    public String name;

    @JsonbProperty("age")
    public int age;
}
