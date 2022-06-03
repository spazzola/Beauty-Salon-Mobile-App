package pomalowane.vacation;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pomalowane.appointment.Appointment;
import pomalowane.appointment.AppointmentDto;
import pomalowane.appointment.CreateAppointmentRequest;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/vacation")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VacationController {

    private VacationService vacationService;

    @PostMapping("/create")
    public void createAppointment(@RequestBody VacationDto vacationDto) throws Exception {
        //logger.info("Tworzenie wizyty: " + createAppointmentRequest);
        Vacation vacation = vacationService.createVacation(vacationDto);
        //logger.info("Utworzono wizyte: " + appointment);

        System.out.println(vacation);
    }

}