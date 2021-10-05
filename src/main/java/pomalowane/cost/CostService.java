package pomalowane.cost;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class CostService {

    private CostDao costDao;
    private CostMapper costMapper;


    @Transactional
    public Cost createCost(CostDto costDto) {
        validateCost(costDto);
        Cost cost = costMapper.fromDto(costDto);

        return costDao.save(cost);
    }

    @Transactional
    public List<Cost> createCost(List<CostDto> costsDto) {
        for (CostDto costDto : costsDto) {
            validateCost(costDto);
        }
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

    @Transactional
    public void deleteCost(Long id) {
        costDao.deleteById(id);
    }

    @Transactional
    public List<Cost> getMonthCosts(int month, int year) {
        return costDao.getMonthCosts(month, year);
    }

    private void validateCost(CostDto costDto) {
        if (costDto.getName() == null || costDto.getName().equals("")) {
            throw new IllegalArgumentException("Bad value of Cost's name: " + costDto.getName());
        }
        if (costDto.getValue() == null || costDto.getValue().equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("Bad value of Cost's value: " + costDto.getValue());
        }
        if (costDto.getAddedDate() == null) {
            throw new IllegalArgumentException("Bad value of Cost's addedDate: " + costDto.getAddedDate());
        }
    }
}