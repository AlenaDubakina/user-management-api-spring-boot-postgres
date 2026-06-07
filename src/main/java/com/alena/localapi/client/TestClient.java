package com.alena.localapi.client;

import com.alena.localapi.config.ApiConfig;
import com.alena.localapi.dto.UserPatchDTO;
import com.alena.localapi.dto.UserRequestDTO;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class TestClient {
    private RequestSpecification request(String token) {
        RequestSpecification spec = given().spec(ApiConfig.requestSpecification);

        if (token != null) {
            spec.header("Authorization", "Bearer " + token);
        }

        return spec;
    }

    public Response get(String endpoint, String token) {
        return request(token)
                .when()
                .get(endpoint);
    }

    public Response get(String endpoint) {
        return get(endpoint, null);
    }

    public Response getById(String endpoint, Long id, String token) {
        return request(token)
                .pathParam("id", id)
                .when()
                .get(endpoint);
    }

    public Response getById(String endpoint, Long id) {
        return getById(endpoint, id, null);
    }

    public Response post(String endpoint, Object body, String token) {
        return request(token)
                .body(body)
                .when()
                .post(endpoint);
    }

    public Response post(String endpoint, Object body) {
        return post(endpoint, body, null);
    }

    public Response delete(String endpoint, Long id, String token) {
        return request(token)
                .pathParam("id", id)
                .when()
                .delete(endpoint);
    }

    public Response delete(String endpoint, Long id) {
        return delete(endpoint, id, null);
    }

    public Response put(String endpoint, Long id, UserRequestDTO userRequestDTO, String token) {
        return request(token)
                .pathParam("id", id)
                .body(userRequestDTO)
                .when()
                .put(endpoint);
    }

    public Response put(String endpoint, Long id, UserRequestDTO userRequestDTO) {
        return put(endpoint, id, userRequestDTO, null);
    }

    public Response patch(String endpoint, Long id, UserPatchDTO patchUpdateUser, String token) {
        return request(token)
                .pathParam("id", id)
                .body(patchUpdateUser)
                .when()
                .patch(endpoint);
    }

    public Response patch(String endpoint, Long id, UserPatchDTO patchUpdateUser) {
        return patch(endpoint, id, patchUpdateUser, null);
    }
}