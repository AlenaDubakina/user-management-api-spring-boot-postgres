package com.alena.localapi.base;

import com.alena.localapi.auth.dto.AuthResponseDTO;
import com.alena.localapi.auth.dto.RegisterRequestDTO;
import com.alena.localapi.client.TestClient;
import com.alena.localapi.config.ApiConfig;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.UserRequestDTO;
import com.alena.localapi.dto.UserResponseDTO;
import com.alena.localapi.entity.UserEntity;
import com.alena.localapi.enums.Role;
import com.alena.localapi.repository.UserRepository;
import com.alena.localapi.security.JwtService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.alena.localapi.factory.AuthFactory.validRegisterRequest;
import static com.alena.localapi.utils.TestUtils.generateRandomEmail;
import static com.alena.localapi.utils.TestUtils.generateRandomPassword;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTest {
    protected static TestClient testClient;
    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected JwtService jwtService;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @LocalServerPort
    private int port;
    private UserEntity userEntity;

    @BeforeAll
    public static void globalSetup() {
        testClient = new TestClient();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    public void setup() {
        ApiConfig.setPort(port);
    }

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    protected UserResponseDTO createUser(UserRequestDTO dto, String token) {
        return testClient.post(ApiEndpoints.USERS, dto, token)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);
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

    protected String getAuthToken() {
        return registerUser(validRegisterRequest()).getToken();
    }

    protected AuthResponseDTO createUserWithRole(Role role) {
        String email = generateRandomEmail();
        String password = generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(password);
        UserEntity userEntity = new UserEntity(email, encodedPassword);
        userEntity.setRole(role);
        userRepository.save(userEntity);

        return new AuthResponseDTO(jwtService.generateToken(email));
    }

    protected String getUserToken() {
        return createUserWithRole(Role.USER).getToken();
    }

    protected String getAdminToken() {
        return createUserWithRole(Role.ADMIN).getToken();
    }
}