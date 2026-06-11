package com.alena.localapi.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiConfig {
    private static final String BASE_URL = "http://localhost";
    private static int port = 8082;

    public static void setPort(int newPort) {
        port = newPort;
        updateRequestSpecification();
    }

    public static void updateRequestSpecification() {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setPort(port)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    public static RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBaseUri(BASE_URL)
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .build();
}