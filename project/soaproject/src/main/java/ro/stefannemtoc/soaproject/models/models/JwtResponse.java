package ro.stefannemtoc.soaproject.models.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private Long id;
    private String username;
    private String email;
    private String role;
    private String type;
}
