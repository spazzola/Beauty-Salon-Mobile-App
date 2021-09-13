package pomalowane.client;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pomalowane.mappers.FromDtoService;

import java.util.List;

@AllArgsConstructor
@Service
public class ClientService {

    private ClientDao clientDao;
    private FromDtoService fromDtoService;


    public Client createClient(ClientDto clientDto) {
        Client client = fromDtoService.fromDto(clientDto);

        return clientDao.save(client);
    }

    @Transactional
    public Client updateClient(ClientDto clientDto) {
        Client client = clientDao.getById(clientDto.getId());

        client.setName(clientDto.getName());
        client.setSurname(clientDto.getSurname());
        client.setPhoneNumber(clientDto.getPhoneNumber());
        client.setMail(clientDto.getMail());

        return clientDao.save(client);
    }

    public Client increaseBelatedCounter(Long clientId) throws Exception {
        Client client = clientDao.findById(clientId)
                .orElseThrow(Exception::new);

        int belatedCounter = client.getBelatedCounter();
        belatedCounter += 1;
        client.setBelatedCounter(belatedCounter);

        return clientDao.save(client);
    }

    public List<Client> getAll() {
        return clientDao.findAll();
    }

}