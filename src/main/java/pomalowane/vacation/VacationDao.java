package pomalowane.vacation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VacationDao extends JpaRepository<Vacation, Long> {


    @Query(value = "SELECT * FROM vacations v " +
            "WHERE user_id = ?1 AND DAY(v.start_Date) = ?2 AND MONTH(v.start_Date) = ?3 AND YEAR(v.start_Date) = ?4",
            nativeQuery = true)
    Optional<List<Vacation>> getVacationsByDate(Long user_id, int day, int month, int year);

}