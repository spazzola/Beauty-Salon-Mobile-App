package pomalowane.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pomalowane.client.Client;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

}