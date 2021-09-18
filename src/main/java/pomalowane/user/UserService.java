package pomalowane.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private UserDao userDao;


    public User createUser(UserDto userDto) {
        validateUser(userDto);
        User user = User.builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .phoneNumber(userDto.getPhoneNumber())
                .mail(userDto.getMail())
                .login(userDto.getLogin())
                .password(userDto.getPassword())
                .role(userDto.getRole())
                .build();

        return userDao.save(user);
    }

    public List<User> getAll() {
        return userDao.findAll();
    }

    private void validateUser(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().equals("")) {
            throw new IllegalArgumentException("Bad value of User's name: " + userDto.getName());
        }
        if (userDto.getSurname() == null || userDto.getSurname().equals("")) {
            throw new IllegalArgumentException("Bad value of User's surname: " + userDto.getSurname());
        }
        if (userDto.getPhoneNumber() == null || userDto.getPhoneNumber().length() < 9) {
            throw new IllegalArgumentException("Bad value of User's phoneNumber: " + userDto.getPhoneNumber());
        }
        if (userDto.getLogin() == null || userDto.getLogin().equals("")) {
            throw new IllegalArgumentException("Bad value of User's login: " + userDto.getLogin());
        }
        if (userDto.getPassword() == null || userDto.getPassword().equals("")) {
            throw new IllegalArgumentException("Bad value of User's password: " + userDto.getPassword());
        }
    }

}