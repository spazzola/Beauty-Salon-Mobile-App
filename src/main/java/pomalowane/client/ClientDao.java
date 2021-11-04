package pomalowane.client;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDao extends JpaRepository<Client, Long> {

    Client findByPhoneNumber(String phoneNumber);
    List<Client> findByIsVisibleTrue();

}