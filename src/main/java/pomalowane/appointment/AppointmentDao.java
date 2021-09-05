package pomalowane.appointment;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentDao extends JpaRepository<Appointment, Long> {

    @Query(value = "SELECT * FROM appointments a " +
            "WHERE MONTH(a.start_Date) = ?1 AND YEAR(a.start_Date) = ?2",
            nativeQuery = true)
    List<Appointment> getMonthAppointments(int month, int year);


    @Query(value = "SELECT * FROM appointments a " +
            "WHERE MONTH(a.start_Date) = ?1 AND YEAR(a.start_Date) = ?2 AND user_id = ?3",
        nativeQuery = true)
    List<Appointment> getUserMonthAppointments(int month, int year, Long user_id);

}