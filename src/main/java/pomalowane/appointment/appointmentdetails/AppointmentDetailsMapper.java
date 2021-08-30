package pomalowane.appointment.appointmentdetails;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pomalowane.appointment.AppointmentDto;
import pomalowane.appointment.AppointmentMapper;
import pomalowane.service.ServiceDto;
import pomalowane.service.ServiceMapper;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class AppointmentDetailsMapper {

    private ServiceMapper serviceMapper;
    private AppointmentMapper appointmentMapper;

    public AppointmentDetailsDto toDto(AppointmentDetails appointmentDetails) {
        AppointmentDto appointmentDto = appointmentMapper.toDto(appointmentDetails.getAppointment());
        ServiceDto serviceDto = serviceMapper.toDto(appointmentDetails.getService());

        return AppointmentDetailsDto.builder()
                .id(appointmentDetails.getId())
                .appointment(appointmentDto)
                .service(serviceDto)
                .build();
    }

    public List<AppointmentDetailsDto> toDto(List<AppointmentDetails> appointmentDetailsList) {
        return appointmentDetailsList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
