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


    @Transactional
    public Client createClient(ClientDto clientDto) {
        validateClient(clientDto);
        Client client = fromDtoService.clientFromDto(clientDto);
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

    @Transactional
    public void deleteClient(Long id) {
        clientDao.deleteById(id);
    }

    @Transactional
    public Client increaseBelatedCounter(Long clientId) throws Exception {
        Client client = clientDao.findById(clientId)
                .orElseThrow(Exception::new);

        int belatedCounter = client.getBelatedCounter();
        belatedCounter += 1;
        client.setBelatedCounter(belatedCounter);

        return clientDao.save(client);
    }

    @Transactional
    public List<Client> getAll() {
        return clientDao.findAll();
    }

    private void validateClient(ClientDto clientDto) {
        if (clientDao.findByPhoneNumber(clientDto.getPhoneNumber()) != null) {
            throw new IllegalArgumentException("Client already exists");
        }
        if (clientDto.getName() == null || clientDto.getName().equals("")) {
            throw new IllegalArgumentException("Bad value of Client's name: " + clientDto.getName());
        }
        if (clientDto.getSurname() == null || clientDto.getSurname().equals("")) {
            throw new IllegalArgumentException("Bad value of Client's surname: " + clientDto.getName());
        }
        if (clientDto.getPhoneNumber() == null || clientDto.getPhoneNumber().length() < 9) {
            throw new IllegalArgumentException("Bad value of Client's phoneNumber: " + clientDto.getName());
        }
    }
}