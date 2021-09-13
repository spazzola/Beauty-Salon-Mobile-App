package pomalowane.cost;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/cost")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CostController {

    private CostMapper costMapper;
    private CostService costService;


    @PostMapping("/create")
    public CostDto createCost(@RequestBody CostDto costDto) {
        Cost cost = costService.createCost(costDto);

        return costMapper.toDto(cost);
    }

    @PostMapping("/createCosts")
    public List<CostDto> createCosts(@RequestBody List<CostDto> costsDto) {
        List<Cost> costs = costService.createCost(costsDto);

        return costMapper.toDto(costs);
    }

    @PutMapping("/update")
    public CostDto updateCost(@RequestBody CostDto costDto) {
        Cost cost = costService.updateCost(costDto);

        return costMapper.toDto(cost);
    }

    @DeleteMapping("/delete")
    public HttpStatus deleteCost(@RequestParam Long id) {
        costService.deleteCost(id);

        return HttpStatus.OK;
    }

    @GetMapping("/getMonthCosts")
    public List<CostDto> getMonthCosts(@RequestParam int month, @RequestParam int year) {
        List<Cost> costs = costService.getMonthCosts(month, year);

        return costMapper.toDto(costs);
    }

}