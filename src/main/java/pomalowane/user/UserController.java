package pomalowane.user;

import lombok.AllArgsConstructor;
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

    @PostMapping("/register")
    public UserDto createUser(@RequestBody UserDto userDto) {
        User user = userService.createUser(userDto);

        return toDtoService.toDto(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        AuthenticationResponse authenticationResponse = userService.authenticateUser(authenticationRequest);

        return ResponseEntity.ok(authenticationResponse);
    }

    @PutMapping("/update")
    public UserDto updateUser(@RequestBody UserDto userDto) throws Exception {
        User user = userService.updateUser(userDto);

        return toDtoService.toDto(user);
    }

    @DeleteMapping("/deleteUser")
    public void deleteUser(@RequestParam("id") Long id) throws Exception {
        userService.deleteUser(id);
    }

    @GetMapping("/getAll")
    public List<UserDto> getAll() {
        List<User> users = userService.getAll();

        return toDtoService.toDto4(users);
    }

}