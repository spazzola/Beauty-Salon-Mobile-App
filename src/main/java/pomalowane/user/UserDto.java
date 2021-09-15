package pomalowane.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {


    private Long id;
    private String login;
    private String password;
    private String name;
    private String surname;
    private String phoneNumber;
    private String mail;
    private String role;
    private double workedHours;

}