package pomalowane.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .password(user.getPassword())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .mail(user.getMail())
                .role(user.getRole())
                .build();
    }

}