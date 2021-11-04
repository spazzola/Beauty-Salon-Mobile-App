package pomalowane.work;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkDao extends JpaRepository<Work, Long> {

    List<Work> findByIsVisibleTrue();

}