package pomalowane.user;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pomalowane.user.jwt.AuthenticationRequest;
import pomalowane.user.jwt.AuthenticationResponse;
import pomalowane.user.jwt.JwtUtil;
import pomalowane.user.userdetails.MyUserDetails;
import pomalowane.user.userdetails.MyUserDetailsService;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private MyUserDetailsService myUserDetailsService;
    private JwtUtil jwtUtil;


    @Transactional
    public User createUser(UserDto userDto) {
        validateUser(userDto);

        final String encryptedPassword = passwordEncoder.encode(userDto.getPassword());

        User user = User.builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .phoneNumber(userDto.getPhoneNumber())
                .mail(userDto.getMail())
                .login(userDto.getLogin())
                .password(encryptedPassword)
                .role(userDto.getRole())
                .build();

        return userDao.save(user);
    }

    public AuthenticationResponse authenticateUser(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getLogin(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            //logger.error("Blad logowania, nieprawidlowe haslo");
            throw new RuntimeException("Nieprawidłowe hasło!");
        }

        MyUserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getLogin());
        boolean isMatch = passwordEncoder.matches(authenticationRequest.getPassword(), userDetails.getPassword());
        String jwt;

        if (!isMatch) {
            //logger.error("Blad logowania, nieprawidlowe haslo");
            throw new RuntimeException("Nieprawidłowe hasło!");
        } else {

            jwt = jwtUtil.generateToken(userDetails);

            //logger.info("Zalogowano");
        }
        String role = myUserDetailsService.extractRole(userDetails.getAuthorities());

        return new AuthenticationResponse(jwt, role, userDetails.getUsername(), userDetails.getName(), userDetails.getSurname());
    }

    @Transactional
    public User updateUser(UserDto userDto) throws Exception {
        User user = userDao.findById(userDto.getId())
                .orElseThrow(Exception::new);

        final String encryptedPassword;
        if (checkIfPasswordChanged(user, userDto)) {
            encryptedPassword = passwordEncoder.encode(userDto.getPassword());
            user.setPassword(encryptedPassword);
        }

        user.setLogin(userDto.getLogin());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setRole(userDto.getRole());

        return user;
    }

    public List<User> getAll() {
        return userDao.findAll();
    }

    @Transactional
    public void deleteUser(Long id) throws Exception {
        User user = userDao.findById(id)
                .orElseThrow(Exception::new);

        userDao.delete(user);
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

    private boolean checkIfPasswordChanged(User user, UserDto userDto) {
        MyUserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getLogin());
        return !passwordEncoder.matches(userDto.getPassword(), userDetails.getPassword());
    }

}