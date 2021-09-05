package pomalowane.cost;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/cost")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CostController {

    private CostMapper costMapper;
    private CostService costService;


    @PostMapping("/create")
    public List<CostDto> createCost(@RequestBody List<CostDto> costsDto) {
        List<Cost> costs = costService.createCost(costsDto);

        return costMapper.toDto(costs);
    }

    @GetMapping("/getMonthCosts")
    public List<CostDto> getMonthCosts(@RequestParam int month, @RequestParam int year) {
        List<Cost> costs = costService.getMonthCosts(month, year);

        return costMapper.toDto(costs);
    }

}