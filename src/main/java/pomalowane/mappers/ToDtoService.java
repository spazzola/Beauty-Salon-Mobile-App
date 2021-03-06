package pomalowane.mappers;

import org.springframework.stereotype.Service;
import pomalowane.appointment.Appointment;
import pomalowane.appointment.AppointmentDto;
import pomalowane.appointment.appointmentdetails.AppointmentDetails;
import pomalowane.appointment.appointmentdetails.AppointmentDetailsDto;
import pomalowane.client.Client;
import pomalowane.client.ClientDto;
import pomalowane.vacation.Vacation;
import pomalowane.vacation.VacationDto;
import pomalowane.work.Work;
import pomalowane.work.WorkDto;
import pomalowane.user.User;
import pomalowane.user.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToDtoService {

    public AppointmentDetailsDto appointmentDetailsToDto(AppointmentDetails appointmentDetails) {
        WorkDto workDto = workToDto(appointmentDetails.getWork());

        return AppointmentDetailsDto.builder()
                .id(appointmentDetails.getId())
                .work(workDto)
                .build();
    }

    public List<AppointmentDto> appointmentToDto(List<Appointment> appointments) {
        return appointments.stream()
                .map(this::appointmentToDto)
                .collect(Collectors.toList());
    }

    public List<AppointmentDetailsDto> appointmentDetailsToDto(List<AppointmentDetails> appointmentDetailsList) {
        return appointmentDetailsList.stream()
                .map(this::appointmentDetailsToDto)
                .collect(Collectors.toList());
    }

    public AppointmentDto appointmentToDto(Appointment appointment) {
        UserDto userDto = userToDto(appointment.getEmployee());
        ClientDto clientDto = clientToDto(appointment.getClient());
        List<AppointmentDetailsDto> appointmentDetailsDto = appointmentDetailsToDto(appointment.getAppointmentDetails());

        return AppointmentDto.builder()
                .id(appointment.getId())
                .startDate(appointment.getStartDate())
                .finishDate(appointment.getFinishDate())
                .note(appointment.getNote())
                .worksSum(appointment.getWorksSum())
                .appointmentDetails(appointmentDetailsDto)
                .client(clientDto)
                .employee(userDto)
                .build();
    }

    public ClientDto clientToDto(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .surname(client.getSurname())
                .phoneNumber(client.getPhoneNumber())
                .belatedCounter(client.getBelatedCounter())
                .build();
    }

    public List<ClientDto> clientToDto(List<Client> clients) {
        return clients.stream()
                .map(this::clientToDto)
                .collect(Collectors.toList());
    }

    public WorkDto workToDto(Work work) {
        return WorkDto.builder()
                .id(work.getId())
                .name(work.getName())
                .price(work.getPrice())
                .hoursDuration(work.getHoursDuration())
                .minutesDuration(work.getMinutesDuration())
                .iconName(work.getIconName())
                .providedPrice(work.getProvidedPrice())
                .build();
    }

    public List<WorkDto> workToDto(List<Work> works) {
        return works.stream()
                .map(this::workToDto)
                .collect(Collectors.toList());
    }

    public UserDto userToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .password(user.getPassword())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .workedHours(user.getWorkedHours())
                .build();
    }

    public List<UserDto> userToDto(List<User> users) {
        return users.stream()
                .map(this::userToDto)
                .collect(Collectors.toList());
    }

    public VacationDto vacationToDto(Vacation vacation) {
        return VacationDto.builder()
                .id(vacation.getId())
                .startDate(vacation.getStartDate())
                .finishDate(vacation.getFinishDate())
                .employee(vacation.getEmployee())
                .build();
    }

    public List<VacationDto> vacationToDto(List<Vacation> vacations) {
        return vacations.stream()
                .map(this::vacationToDto)
                .collect(Collectors.toList());
    }

}