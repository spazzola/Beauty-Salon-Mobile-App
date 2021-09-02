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

    private WorkDao workDao;

    public List<AppointmentDetails> fromDto(List<AppointmentDetailsDto> appointmentDetailsDtoList, Appointment appointment) throws Exception {
        List<AppointmentDetails> appointmentDetailsList = new ArrayList<>();
        for (AppointmentDetailsDto appointmentDetailsDto : appointmentDetailsDtoList) {
            Work work = workDao.findById(appointmentDetailsDto.getWork().getId())
                    .orElseThrow(Exception::new);

            AppointmentDetails appointmentDetails = AppointmentDetails.builder()
                    .id(appointmentDetailsDto.getId())
                    .work(work)
                    .appointment(appointment)
                    .build();

            appointmentDetailsList.add(appointmentDetails);
        }

        return appointmentDetailsList;
    }

    public Client fromDto(ClientDto clientDto) {
        return Client.builder()
                .id(clientDto.getId())
                .name(clientDto.getName())
                .surname(clientDto.getSurname())
                .phoneNumber(clientDto.getPhoneNumber())
                .mail(clientDto.getMail())
                .build();
    }

    public User fromDto(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .login(userDto.getLogin())
                .password(userDto.getPassword())
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .phoneNumber(userDto.getPhoneNumber())
                .mail(userDto.getMail())
                .role(userDto.getRole())
                .build();

    }

}