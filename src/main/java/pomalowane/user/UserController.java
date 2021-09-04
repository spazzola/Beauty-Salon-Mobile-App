package pomalowane.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pomalowane.mappers.ToDtoService;

import java.util.List;

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

    @GetMapping("/getAll")
    public List<UserDto> getAll() {
        List<User> users = userService.getAll();

        return toDtoService.toDto4(users);
    }

}