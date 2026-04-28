package com.alena.localapi.base;

import com.alena.localapi.client.TestClient;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    protected static TestClient testClient;

    @BeforeAll
    public static void setup() {
        testClient = new TestClient();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}