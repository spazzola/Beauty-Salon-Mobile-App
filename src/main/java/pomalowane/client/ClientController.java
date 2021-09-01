package pomalowane.client;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pomalowane.mappers.ToDtoService;

@AllArgsConstructor
@RestController
@RequestMapping("/client")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClientController {

    private ToDtoService toDtoService;
    private ClientService clientService;

    @PostMapping("/create")
    public ClientDto createClient(@RequestBody ClientDto clientDto) {
        Client client = clientService.createClient(clientDto);

        return toDtoService.toDto(client);
    }

}