package pomalowane.client;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pomalowane.mappers.FromDtoService;

@AllArgsConstructor
@Service
public class ClientService {

    private ClientDao clientDao;
    private FromDtoService fromDtoService;


    public Client createClient(ClientDto clientDto) {
        Client client = fromDtoService.fromDto(clientDto);

        return clientDao.save(client);
    }

}