package pomalowane.client;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pomalowane.mappers.FromDtoService;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ClientService {

    private ClientDao clientDao;
    private FromDtoService fromDtoService;
    private static final Logger logger = LogManager.getLogger(ClientService.class);


    @Transactional
    public Client create(ClientDto clientDto) {
        validateClient(clientDto);
        Client client = fromDtoService.clientFromDto(clientDto);
        client.setVisible(true);
        return saveIfNotExist(client);
    }

    @Transactional
    public List<Client> create(List<ClientDto> clients) {
        List<Client> resultList = new ArrayList<>();
        for (ClientDto clientDto : clients) {
            Client client = create(clientDto);
            resultList.add(client);
        }
        return resultList;
    }

    @Transactional
    public Client updateClient(ClientDto clientDto) {
        Client client = clientDao.getById(clientDto.getId());
        logger.info("Klient przed aktualizacja: " + client);
        client.setName(clientDto.getName());
        client.setSurname(clientDto.getSurname());
        client.setPhoneNumber(clientDto.getPhoneNumber());

        return clientDao.save(client);
    }

    //@Transactional
    public void deleteClient(Long id) {
        try {
            clientDao.deleteById(id);
        } catch (Exception exception) {
            Client client = clientDao.getById(id);
            client.setVisible(false);
            clientDao.save(client);
        }
    }

    @Transactional
    public Client increaseBelatedCounter(Long clientId) throws Exception {
        Client client = clientDao.findById(clientId)
                .orElseThrow(Exception::new);
        logger.info("Klient przed dodaniem spoznienia: " + client);

        int belatedCounter = client.getBelatedCounter();
        belatedCounter += 1;
        client.setBelatedCounter(belatedCounter);

        return clientDao.save(client);
    }

    @Transactional
    public List<Client> getAll() {
        return clientDao.findByIsVisibleTrue();
    }

    private void validateClient(ClientDto clientDto) {
        if (clientDto.getName() == null) {
            throw new IllegalArgumentException("Bad value of Client's name: " + clientDto.getName());
        }
//        if (clientDto.getSurname() == null || clientDto.getSurname().equals("")) {
//            throw new IllegalArgumentException("Bad value of Client's surname: " + clientDto.getSurname());
//        }
//        if (clientDto.getPhoneNumber() == null || clientDto.getPhoneNumber().length() < 9) {
//            throw new IllegalArgumentException("Bad value of Client's phoneNumber: " + clientDto.getPhoneNumber());
//        }
    }

    private Client saveIfNotExist(Client client) {
        if (clientDao.findByPhoneNumber(client.getPhoneNumber()) == null) {
            return clientDao.save(client);
        } else {
            return client;
        }
    }
}