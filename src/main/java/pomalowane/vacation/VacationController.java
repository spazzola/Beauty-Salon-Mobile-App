package pomalowane.vacation;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import pomalowane.mappers.ToDtoService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/vacation")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VacationController {

    private ToDtoService toDtoService;
    private VacationService vacationService;

    private static final Logger logger = LogManager.getLogger(VacationController.class);

    @PostMapping("/create")
    public VacationDto createAppointment(@RequestBody VacationDto vacationDto) throws Exception {
        logger.info("Tworzenie urlopu: " + vacationDto);
        Vacation vacation = vacationService.createVacation(vacationDto);
        logger.info("Utworzono urlop: " + vacation);

        return toDtoService.vacationToDto(vacation);
    }

    @GetMapping("/getAll")
    public List<VacationDto> getAll() {
        List<Vacation> vacations = vacationService.getAllVacations();

        return toDtoService.vacationToDto(vacations);
    }

}