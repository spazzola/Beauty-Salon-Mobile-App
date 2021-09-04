package pomalowane.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pomalowane.appointment.appointmentdetails.AppointmentDetailsDto;
import pomalowane.client.Client;
import pomalowane.client.ClientDto;
import pomalowane.user.User;
import pomalowane.user.UserDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {


    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private List<AppointmentDetailsDto> appointmentDetails;
    private String note;
    private BigDecimal worksSum;
    private ClientDto client;
    private UserDto employee;

}