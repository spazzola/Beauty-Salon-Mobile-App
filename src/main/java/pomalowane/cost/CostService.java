package pomalowane.cost;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class CostService {

    private CostDao costDao;
    private CostMapper costMapper;


    public Cost createCost(CostDto costDto) {
        Cost cost = costMapper.fromDto(costDto);

        return costDao.save(cost);
    }

    public List<Cost> createCost(List<CostDto> costsDto) {
        List<Cost> costs = costMapper.fromDto(costsDto);

        return costDao.saveAll(costs);
    }

    @Transactional
    public Cost updateCost(CostDto costDto) {
        Cost cost = costDao.getById(costDto.getId());
        cost.setAddedDate(costDto.getAddedDate());
        cost.setName(costDto.getName());
        cost.setValue(costDto.getValue());

        return costDao.save(cost);
    }

    public void deleteCost(Long id) {
        costDao.deleteById(id);
    }

    public List<Cost> getMonthCosts(int month, int year) {
        return costDao.getMonthCosts(month, year);
    }

}