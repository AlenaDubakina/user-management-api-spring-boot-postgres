package com.alena.localapi.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserPatchDTO {
    @Pattern(regexp = ".+@.+\\..+")
    private String email;

    private String password;
}