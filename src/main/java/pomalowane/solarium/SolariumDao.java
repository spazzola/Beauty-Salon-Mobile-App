package pomalowane.solarium;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolariumDao extends JpaRepository<Solarium, Long> {

    @Query(value = "SELECT * FROM solariums s " +
            "WHERE MONTH(s.used_Date) = ?1 AND YEAR(s.used_Date) = ?2",
            nativeQuery = true)
    Solarium getMonthSolarium(int month, int year);

}