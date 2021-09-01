package pomalowane.mappers;

import org.springframework.stereotype.Service;
import pomalowane.appointment.Appointment;
import pomalowane.appointment.AppointmentDto;
import pomalowane.appointment.appointmentdetails.AppointmentDetails;
import pomalowane.appointment.appointmentdetails.AppointmentDetailsDto;
import pomalowane.client.Client;
import pomalowane.client.ClientDto;
import pomalowane.work.Work;
import pomalowane.work.WorkDto;
import pomalowane.user.User;
import pomalowane.user.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToDtoService {

    public AppointmentDetailsDto toDto(AppointmentDetails appointmentDetails) {
        //AppointmentDto appointmentDto = toDto(appointmentDetails.getAppointment());
        WorkDto workDto = toDto(appointmentDetails.getWork());

        return AppointmentDetailsDto.builder()
                .id(appointmentDetails.getId())
                //.appointment(appointmentDto)
                .service(workDto)
                .build();
    }

    public List<AppointmentDetailsDto> toDto(List<AppointmentDetails> appointmentDetailsList) {
        return appointmentDetailsList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public AppointmentDto toDto(Appointment appointment) {
        UserDto userDto = toDto(appointment.getEmployee());
        ClientDto clientDto = toDto(appointment.getClient());
        List<AppointmentDetailsDto> appointmentDetailsDto = toDto(appointment.getAppointmentDetails());
        //set to appointmentdetailsdto an appointment in loop?

        return AppointmentDto.builder()
                .id(appointment.getId())
                .startDate(appointment.getStartDate())
                .finishDate(appointment.getFinishDate())
                .appointmentDetails(appointmentDetailsDto)
                .client(clientDto)
                .employee(userDto)
                .build();
    }

    public ClientDto toDto(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .surname(client.getSurname())
                .phoneNumber(client.getPhoneNumber())
                .mail(client.getMail())
                .build();
    }

    public WorkDto toDto(Work work) {
        WorkDto workDto = WorkDto.builder()
                .id(work.getId())
                .name(work.getName())
                .price(work.getPrice())
                .hoursDuration(work.getHoursDuration())
                .minutesDuration(work.getMinutesDuration())

                .build();

        //if (work.getAppointmentDetails() != null) {
            //List<AppointmentDetailsDto> appointmentDetailsDto = toDto(work.getAppointmentDetails());
            //workDto.setAppointmentDetails(appointmentDetailsDto);
        //}

        return workDto;
    }

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .password(user.getPassword())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .mail(user.getMail())
                .role(user.getRole())
                .build();
    }

}