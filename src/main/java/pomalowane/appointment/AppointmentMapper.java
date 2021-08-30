package pomalowane.appointment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pomalowane.appointment.appointmentdetails.AppointmentDetailsDto;
import pomalowane.appointment.appointmentdetails.AppointmentDetailsMapper;
import pomalowane.client.ClientDto;
import pomalowane.client.ClientMapper;
import pomalowane.user.UserDto;
import pomalowane.user.UserMapper;

import java.util.List;

@AllArgsConstructor
@Component
public class AppointmentMapper {

    private UserMapper userMapper;
    private ClientMapper clientMapper;
    private AppointmentDetailsMapper appointmentDetailsMapper;

    public AppointmentDto toDto(Appointment appointment) {
        UserDto userDto = userMapper.toDto(appointment.getEmployee());
        ClientDto clientDto = clientMapper.toDto(appointment.getClient());
        List<AppointmentDetailsDto> appointmentDetailsDto = appointmentDetailsMapper.toDto(appointment.getAppointmentDetails());

        return AppointmentDto.builder()
                .id(appointment.getId())
                .startDate(appointment.getStartDate())
                .finishDate(appointment.getFinishDate())
                .appointmentDetails(appointmentDetailsDto)
                .client(clientDto)
                .employee(userDto)
                .build();
    }

}