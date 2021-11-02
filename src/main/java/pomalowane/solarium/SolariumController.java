package pomalowane.solarium;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import pomalowane.user.UserController;

@AllArgsConstructor
@RestController
@RequestMapping("/solarium")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SolariumController {

    private SolariumMapper solariumMapper;
    private SolariumService solariumService;
    private static final Logger logger = LogManager.getLogger(SolariumController.class);

    @PostMapping("/use")
    public SolariumDto useSolarium(@RequestBody SolariumDto solariumDto) throws Exception {
        logger.info("Dodawanie uzycie solarium: " + solariumDto);
        Solarium solarium = solariumService.useSolarium(solariumDto);
        logger.info("Dodano uzycie solarium: " + solarium);

        return solariumMapper.toDto(solarium);
    }

    @GetMapping("/getMonthSolarium")
    public SolariumDto getMonthSolarium(@RequestParam int month, @RequestParam int year) {
        Solarium solarium = solariumService.getMonthSolarium(month, year);

        return solariumMapper.toDto(solarium);
    }

}