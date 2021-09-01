package pomalowane.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pomalowane.mappers.ToDtoService;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private UserService userService;
    private ToDtoService toDtoService;

    @PostMapping("/create")
    public UserDto createUser(@RequestBody UserDto userDto) {
        User user = userService.createUser(userDto);

        return toDtoService.toDto(user);
    }

}