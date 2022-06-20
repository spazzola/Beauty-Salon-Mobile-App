package pomalowane.appointment;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pomalowane.mappers.ToDtoService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/appointment")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AppointmentController {

    private AppointmentService appointmentService;
    private ToDtoService toDtoService;
    private static final Logger logger = LogManager.getLogger(AppointmentController.class);

    @PostMapping("/create")
    public AppointmentDto createAppointment(@RequestBody CreateAppointmentRequest createAppointmentRequest) throws Exception {
        logger.info("Tworzenie wizyty: " + createAppointmentRequest);
        Appointment appointment = appointmentService.createAppointment(createAppointmentRequest);
        logger.info("Utworzono wizyte: " + appointment);

        return toDtoService.appointmentToDto(appointment);
    }

    @GetMapping("/getMonthAppointments")
    public List<AppointmentDto> getMonthAppointments(@RequestParam int month,
                                                     @RequestParam int year,
                                                     @RequestParam Long userId) throws Exception {
        List<Appointment> appointments = appointmentService.getMonthAppointments(month, year, userId);

        return toDtoService.appointmentToDto(appointments);

    }

    @GetMapping("/getIncomingAppointments")
    public List<AppointmentDto> getIncomingAppointments(@RequestParam long clientId) {
        List<Appointment> appointments = appointmentService.getIncomingAppointments(clientId);

        return toDtoService.appointmentToDto(appointments);
    }

    @PutMapping("/update")
    public AppointmentDto updateAppointment(@RequestBody UpdateAppointmentRequest updateAppointmentRequest) throws Exception {
        logger.info("Aktualizowanie wizyty: " + updateAppointmentRequest);
        Appointment appointment = appointmentService.updateAppointment(updateAppointmentRequest);
        logger.info("Zaktualizowano wizyte: " + appointment);

        return toDtoService.appointmentToDto(appointment);
    }

    @DeleteMapping("/delete")
    public HttpStatus deleteAppointment(@RequestParam Long id) throws Exception {
        logger.info("Usuwanie wizyty o id: " + id);
        appointmentService.deleteAppointment(id);
        logger.info("Usunieto wizyte");

        return HttpStatus.OK;
    }

    @GetMapping("/getAll")
    public List<AppointmentDto> getAll() {
        List<Appointment> appointments = appointmentService.getAll();

        return toDtoService.appointmentToDto(appointments);
    }

}