package com.subh.TestNest.Dto;

import com.subh.TestNest.Entity.Role;
import lombok.Data;

@Data
public class RegisterRequest {
        private String fullName;
        private String email;
        private String password;
        private Role role; // ADMIN or STUDENT
        private String universityName;
}
