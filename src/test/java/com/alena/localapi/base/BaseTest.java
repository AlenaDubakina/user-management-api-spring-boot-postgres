package com.alena.localapi.base;

import com.alena.localapi.auth.dto.AuthResponseDTO;
import com.alena.localapi.auth.dto.RegisterRequestDTO;
import com.alena.localapi.client.TestClient;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.UserRequestDTO;
import com.alena.localapi.dto.UserResponseDTO;
import com.alena.localapi.repository.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTest {
    protected TestClient testClient;
    @Autowired
    protected UserRepository userRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        testClient = new TestClient();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    protected UserResponseDTO createUser(UserRequestDTO dto) {
        return testClient.post(ApiEndpoints.USERS, dto)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);
    }

    protected AuthResponseDTO registerUser(RegisterRequestDTO dto) {
        return testClient.post(ApiEndpoints.AUTH_REGISTER, dto)
                .then()
                .statusCode(201)
                .extract()
                .as(AuthResponseDTO.class);
    }
}