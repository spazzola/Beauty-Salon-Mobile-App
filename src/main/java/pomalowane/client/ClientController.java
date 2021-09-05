package pomalowane.client;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pomalowane.mappers.ToDtoService;

import java.util.List;

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

    @PutMapping("/belated")
    public ClientDto belated(@RequestParam Long clientId) throws Exception {
        Client client = clientService.increaseBelatedCounter(clientId);

        return toDtoService.toDto(client);
    }

    @GetMapping("/getAll")
    public List<ClientDto> getAll() {
        List<Client> clients = clientService.getAll();

        return toDtoService.toDto3(clients);
    }

}