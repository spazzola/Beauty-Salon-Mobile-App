package pomalowane.user.jwt;

import lombok.Data;

@Data
public class AuthenticationResponse {

    private final String jwt;
    private final String role;
    private final String login;
    private final String name;
    private final String surname;

}