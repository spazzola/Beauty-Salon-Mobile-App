package pomalowane.appointment.appointmentdetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentDetailsDao extends JpaRepository<AppointmentDetails, Long> {

    @Modifying
    @Query(value = "DELETE FROM appointment_details ad WHERE ad.work_fk = ?1",
            nativeQuery = true)
    void deleteByWorkId(Long workId);

}