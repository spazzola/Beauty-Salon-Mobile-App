package pomalowane.work;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pomalowane.appointment.appointmentdetails.AppointmentDetailsDto;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private int hoursDuration;
    private int minutesDuration;
    private List<AppointmentDetailsDto> appointmentDetails;

}