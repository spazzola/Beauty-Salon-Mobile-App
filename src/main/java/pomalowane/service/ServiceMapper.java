package pomalowane.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pomalowane.appointment.appointmentdetails.AppointmentDetails;
import pomalowane.appointment.appointmentdetails.AppointmentDetailsDto;
import pomalowane.appointment.appointmentdetails.AppointmentDetailsMapper;

import java.util.List;

@AllArgsConstructor
@Component
public class ServiceMapper {

    private AppointmentDetailsMapper appointmentDetailsMapper;

    public ServiceDto toDto(Service service) {
        List<AppointmentDetailsDto> appointmentDetailsDto = appointmentDetailsMapper.toDto(service.getAppointmentDetails());

        return ServiceDto.builder()
                .id(service.getId())
                .name(service.getName())
                .price(service.getPrice())
                .hoursDuration(service.getHoursDuration())
                .minutesDuration(service.getMinutesDuration())
                .appointmentDetails(appointmentDetailsDto)
                .build();
    }

}
