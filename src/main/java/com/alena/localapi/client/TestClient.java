package com.alena.localapi.client;

import com.alena.localapi.config.ApiConfig;
import com.alena.localapi.dto.UserPatchDTO;
import com.alena.localapi.dto.UserRequestDTO;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class TestClient {
    public Response get(String endpoint) {
        return given().spec(ApiConfig.requestSpecification)
                .when()
                .get(endpoint);
    }

    public Response getById(String endpoint, Long id) {
        return given().spec(ApiConfig.requestSpecification)
                .pathParam("id", id)
                .when()
                .get(endpoint);
    }

    public Response post(String endpoint, UserRequestDTO userRequestDTO) {
        return given().spec(ApiConfig.requestSpecification)
                .body(userRequestDTO)
                .when()
                .post(endpoint);
    }

    public Response delete(String endpoint, Long id) {
        return given()
                .spec(ApiConfig.requestSpecification)
                .pathParam("id", id)
                .when()
                .delete(endpoint);
    }

    public Response put(String endpoint, Long id, UserRequestDTO userRequestDTO) {
        return given()
                .spec(ApiConfig.requestSpecification)
                .pathParam("id", id)
                .body(userRequestDTO)
                .when()
                .put(endpoint);
    }

    public Response patch(String endpoint, Long id, UserPatchDTO patchUpdateUser) {
        return given()
                .spec(ApiConfig.requestSpecification)
                .pathParam("id", id)
                .body(patchUpdateUser)
                .when()
                .patch(endpoint);
    }
}