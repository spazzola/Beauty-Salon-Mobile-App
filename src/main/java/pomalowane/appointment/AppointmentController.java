package pomalowane.appointment;

import lombok.AllArgsConstructor;
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

    @GetMapping("/getAll")
    public List<AppointmentDto> getAll() {
        List<Appointment> appointments = appointmentService.getAll();

        return toDtoService.toDto2(appointments);
    }

}