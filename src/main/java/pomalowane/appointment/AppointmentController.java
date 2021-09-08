package pomalowane.appointment;

import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
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

    @PostMapping("/create")
    public AppointmentDto createAppointment(@RequestBody CreateAppointmentRequest createAppointmentRequest) throws Exception {
        Appointment appointment = appointmentService.createAppointment(createAppointmentRequest);

        return toDtoService.toDto(appointment);
    }

    @GetMapping("/getMonthAppointments")
    public List<AppointmentDto> getMonthAppointments(@RequestParam int month,
                                                     @RequestParam int year,
                                                     @RequestParam Long userId) throws Exception {
        List<Appointment> appointments = appointmentService.getMonthAppointments(month, year, userId);

        return toDtoService.toDto2(appointments);

    }

    @PutMapping("/update")
    public AppointmentDto updateAppointment(@RequestBody UpdateAppointmentRequest updateAppointmentRequest) throws Exception {
        Appointment appointment = appointmentService.updateAppointment(updateAppointmentRequest);

        return toDtoService.toDto(appointment);
    }

    @DeleteMapping("/delete")
    public HttpStatus deleteAppointment(@RequestParam Long id) {
        appointmentService.deleteAppointment(id);

        return HttpStatus.OK;
    }

    @GetMapping("/getAll")
    public List<AppointmentDto> getAll() {
        List<Appointment> appointments = appointmentService.getAll();

        return toDtoService.toDto2(appointments);
    }

}