package pomalowane.cost;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CostService {

    private CostDao costDao;
    private CostMapper costMapper;

    public List<Cost> createCost(List<CostDto> costsDto) {
        List<Cost> costs = costMapper.fromDto(costsDto);

        return costDao.saveAll(costs);
    }

    public List<Cost> getMonthCosts(int month, int year) {
        return costDao.getMonthCosts(month, year);
    }

}