package pomalowane.cost;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CostDao extends JpaRepository<Cost, Long> {

    @Query(value = "SELECT * FROM costs c " +
            "WHERE MONTH(c.added_Date) = ?1 AND YEAR(c.added_Date) = ?2",
            nativeQuery = true)
    List<Cost> getMonthCosts(int month, int year);

}