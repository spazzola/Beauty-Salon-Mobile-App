package pomalowane.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pomalowane.appointment.Appointment;
import pomalowane.appointment.appointmentdetails.AppointmentDetails;
import pomalowane.appointment.appointmentdetails.AppointmentDetailsDto;
import pomalowane.client.Client;
import pomalowane.client.ClientDto;
import pomalowane.work.Work;
import pomalowane.work.WorkDao;
import pomalowane.user.User;
import pomalowane.user.UserDto;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class FromDtoService {


    public Client clientFromDto(ClientDto clientDto) {
        return Client.builder()
                .id(clientDto.getId())
                .name(clientDto.getName())
                .surname(clientDto.getSurname())
                .phoneNumber(clientDto.getPhoneNumber())
                .belatedCounter(clientDto.getBelatedCounter())
                .isVisible(true)
                .build();
    }

}