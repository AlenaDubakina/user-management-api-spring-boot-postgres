package com.alena.localapi.base;

import com.alena.localapi.client.TestClient;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.UserRequestDTO;
import com.alena.localapi.dto.UserResponseDTO;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.List;

public class BaseTest {
    protected static TestClient testClient;
    protected List<Long> createdUsersId = new ArrayList<>();

    @BeforeAll
    public static void setup() {
        testClient = new TestClient();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterEach
    public void cleanup() {
        createdUsersId.forEach(userId -> {
            try {
                testClient.delete(ApiEndpoints.USERS_BY_ID, userId);
            } catch (Exception ignored) {
            }
        });
        createdUsersId.clear();
    }

    protected UserResponseDTO createUser(UserRequestDTO dto) {
        UserResponseDTO userResponseDTO = testClient.post(ApiEndpoints.USERS, dto)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        if (userResponseDTO.getId() != null) {
            createdUsersId.add(userResponseDTO.getId());
        }

        return userResponseDTO;
    }
}