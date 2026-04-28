package com.alena.localapi.positive;

import com.alena.localapi.base.BaseTest;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.UserRequestDTO;
import com.alena.localapi.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserApiPositiveTest extends BaseTest {

    @Test
    public void createUserTest() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("test@mail.com", "12345");

        UserResponseDTO user = testClient.postRequest(ApiEndpoints.USERS, userRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        assertThat(user.getId())
                .as("У пользователя не присвоен id")
                .isPositive();

        assertThat(user.getEmail())
                .as("У пользователей невалидный email")
                .isEqualTo(userRequestDTO.getEmail());
    }
}