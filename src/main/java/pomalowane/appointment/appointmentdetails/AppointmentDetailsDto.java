package pomalowane.appointment.appointmentdetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pomalowane.appointment.AppointmentDto;
import pomalowane.work.WorkDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDetailsDto {

    private Long id;
    private WorkDto service;

}