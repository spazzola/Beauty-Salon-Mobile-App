package pomalowane.client;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
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
    private static final Logger logger = LogManager.getLogger(ClientController.class);

    @PostMapping("/create")
    public ClientDto createClient(@RequestBody ClientDto clientDto) {
        logger.info("Tworzenie klienta: " + clientDto);
        Client client = clientService.create(clientDto);
        logger.info("Utworzono klienta: " + client);

        return toDtoService.clientToDto(client);
    }

    @PostMapping("/createAll")
    public List<ClientDto> createClients(@RequestBody List<ClientDto> clientsDto) {
        logger.info("Tworzenie klienta: " + clientsDto);
        List<Client> clients = clientService.create(clientsDto);
        logger.info("Utworzono klienta: " + clients);

        return toDtoService.clientToDto(clients);
    }

    @PutMapping("/update")
    public ClientDto updateClient(@RequestBody ClientDto clientDto) {
        logger.info("Aktualizowanie klienta: " + clientDto);
        Client client = clientService.updateClient(clientDto);
        logger.info("Zaktualizowano klienta: " + client);

        return toDtoService.clientToDto(client);
    }

    @DeleteMapping("/delete")
    public HttpStatus deleteClient(@RequestParam Long id) {
        logger.info("Usuwanie klienta o id: " + id);
        clientService.deleteClient(id);
        logger.info("Usunieto klienta");

        return HttpStatus.OK;
    }

    @PutMapping("/belated")
    public ClientDto belated(@RequestParam Long id) throws Exception {
        logger.info("Dodawanie spoznienia dla klienta o id: " + id);
        Client client = clientService.increaseBelatedCounter(id);
        logger.info("Dodano spoznienie dla klienta: " + client);

        return toDtoService.clientToDto(client);
    }

    @GetMapping("/getAll")
    public List<ClientDto> getAll() {
        List<Client> clients = clientService.getAll();

        return toDtoService.clientToDto(clients);
    }

}