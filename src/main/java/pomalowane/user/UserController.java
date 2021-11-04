package pomalowane.user;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pomalowane.mappers.ToDtoService;
import pomalowane.user.jwt.AuthenticationRequest;
import pomalowane.user.jwt.AuthenticationResponse;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private UserService userService;
    private ToDtoService toDtoService;
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @PostMapping("/register")
    public UserDto createUser(@RequestBody UserDto userDto) {
        logger.info("Tworzenie uzytkownika: " + userDto.toString());
        User user = userService.createUser(userDto);
        logger.info("Utworzono uzytkownika: " + user.toString());

        return toDtoService.userToDto(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        AuthenticationResponse authenticationResponse = userService.authenticateUser(authenticationRequest);

        return ResponseEntity.ok(authenticationResponse);
    }

    @PutMapping("/update")
    public UserDto updateUser(@RequestBody UserDto userDto) throws Exception {
        logger.info("Aktualizowanie uzytkownika: " + userDto.toString());
        User user = userService.updateUser(userDto);
        logger.info("Zaktualizowano uzytkownika: " + user.toString());

        return toDtoService.userToDto(user);
    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestParam("id") Long id) throws Exception {
        logger.info("Usuwanie uzytkownika o id: " + id);
        userService.deleteUser(id);
        logger.info("Usunieto uzytkownika");
    }

    @GetMapping("/getAll")
    public List<UserDto> getAll() {
        List<User> users = userService.getAll();

        return toDtoService.userToDto(users);
    }

}