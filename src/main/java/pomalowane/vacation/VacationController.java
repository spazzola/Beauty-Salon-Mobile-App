package pomalowane.vacation;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
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
    public VacationDto createAppointment(@RequestBody CreateVacationRequest createVacationRequest) throws Exception {
        logger.info("Tworzenie urlopu: " + createVacationRequest);
        Vacation vacation = vacationService.createVacation(createVacationRequest);
        logger.info("Utworzono urlop: " + vacation);

        return toDtoService.vacationToDto(vacation);
    }

    @GetMapping("/getAll")
    public List<VacationDto> getAll() {
        List<Vacation> vacations = vacationService.getAllVacations();

        return toDtoService.vacationToDto(vacations);
    }

    @DeleteMapping("/delete")
    public HttpStatus deleteAppointment(@RequestParam Long id) {
        logger.info("Usuwanie urlopu o id: " + id);
        vacationService.deleteVacation(id);
        logger.info("Usunieto urlop");

        return HttpStatus.OK;
    }

    @PutMapping("/update")
    public VacationDto updateClient(@RequestBody UpdateVacationRequest updateVacationRequest) {
        logger.info("Aktualizowanie urlopu: " + updateVacationRequest);
        Vacation vacation = vacationService.updateVacation(updateVacationRequest);
        logger.info("Zaktualizowano urlop: " + vacation);

        return toDtoService.vacationToDto(vacation);
    }

}