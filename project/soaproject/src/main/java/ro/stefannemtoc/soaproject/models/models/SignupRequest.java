package ro.stefannemtoc.soaproject.models.models;

import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private String role;
}
