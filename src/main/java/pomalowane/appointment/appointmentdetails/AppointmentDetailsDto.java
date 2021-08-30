package pomalowane.appointment.appointmentdetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pomalowane.appointment.Appointment;
import pomalowane.appointment.AppointmentDto;
import pomalowane.service.Service;
import pomalowane.service.ServiceDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDetailsDto {

    private Long id;
    private AppointmentDto appointment;
    private ServiceDto service;

}
