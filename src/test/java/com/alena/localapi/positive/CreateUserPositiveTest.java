package com.alena.localapi.positive;

import com.alena.localapi.base.BaseTest;
import com.alena.localapi.dto.UserRequestDTO;
import com.alena.localapi.dto.UserResponseDTO;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static com.alena.localapi.assertions.UserAssertions.assertUserEmailIsEquals;
import static com.alena.localapi.assertions.UserAssertions.assertUserHasRequiredFields;
import static com.alena.localapi.factory.UserFactory.customUser;

public class CreateUserPositiveTest extends BaseTest {
    @ParameterizedTest
    @MethodSource("com.alena.localapi.providers.user.UserDataProvider#validUserData")
    public void createUser(String email, String password) {
        UserRequestDTO userRequestDTO = customUser(email, password);

        UserResponseDTO user = createUser(userRequestDTO, getUserToken());

        assertUserHasRequiredFields(user);
        assertUserEmailIsEquals(user, userRequestDTO.getEmail());
    }
}