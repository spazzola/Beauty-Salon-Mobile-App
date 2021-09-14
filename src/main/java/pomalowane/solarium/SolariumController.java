package pomalowane.solarium;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/solarium")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SolariumController {

    private SolariumMapper solariumMapper;
    private SolariumService solariumService;


    @PostMapping("/use")
    public SolariumDto useSolarium(@RequestBody SolariumDto solariumDto) {
        Solarium solarium = solariumService.useSolarium(solariumDto);

        return solariumMapper.toDto(solarium);
    }

    @GetMapping("/getMonthSolarium")
    public SolariumDto getMonthSolarium(@RequestParam int month, @RequestParam int year) {
        Solarium solarium = solariumService.getMonthSolarium(month, year);

        return solariumMapper.toDto(solarium);
    }

}