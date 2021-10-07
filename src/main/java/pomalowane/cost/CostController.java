package pomalowane.cost;

import java.util.List;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/cost")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CostController {

    private CostMapper costMapper;
    private CostService costService;
    private static final Logger logger = LogManager.getLogger(CostController.class);

    @PostMapping("/create")
    public CostDto createCost(@RequestBody CostDto costDto) {
        logger.info("Tworzenie kosztu: " + costDto);
        Cost cost = costService.createCost(costDto);
        logger.info("Utworzono koszt: " + cost);

        return costMapper.toDto(cost);
    }

    @PostMapping("/createCosts")
    public List<CostDto> createCosts(@RequestBody List<CostDto> costsDto) {
        logger.info("Tworzenie kosztow: " + costsDto);
        List<Cost> costs = costService.createCost(costsDto);
        logger.info("Utworzono koszty: " + costs);

        return costMapper.toDto(costs);
    }

    @PutMapping("/update")
    public CostDto updateCost(@RequestBody CostDto costDto) {
        logger.info("Aktualizowanie kosztu: " + costDto);
        Cost cost = costService.updateCost(costDto);
        logger.info("Zaktualizowano koszt: " + cost);

        return costMapper.toDto(cost);
    }

    @DeleteMapping("/delete")
    public HttpStatus deleteCost(@RequestParam Long id) {
        logger.info("Usuwanie kosztu o id: " + id);
        costService.deleteCost(id);
        logger.info("Usunieto koszt");

        return HttpStatus.OK;
    }

    @GetMapping("/getMonthCosts")
    public List<CostDto> getMonthCosts(@RequestParam int month, @RequestParam int year) {
        List<Cost> costs = costService.getMonthCosts(month, year);

        return costMapper.toDto(costs);
    }

}