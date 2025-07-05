package com.subh.TestNest.Dto.login;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String role;
}
