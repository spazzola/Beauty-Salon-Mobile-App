package pomalowane.sms;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsDao extends JpaRepository<Sms, Long> {

    List<Sms> findByAppointmentId(Long id);
    void deleteByAppointmentId(Long id);

}