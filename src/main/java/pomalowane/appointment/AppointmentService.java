package pomalowane.appointment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pomalowane.appointment.appointmentdetails.AppointmentDetails;
import pomalowane.appointment.appointmentdetails.AppointmentDetailsDto;
import pomalowane.appointment.appointmentdetails.AppointmentDetailsMapper;
import pomalowane.client.Client;
import pomalowane.client.ClientDao;
import pomalowane.client.ClientMapper;
import pomalowane.mappers.FromDtoService;
import pomalowane.user.UserDao;
import pomalowane.work.Work;
import pomalowane.work.WorkDao;
import pomalowane.user.User;
import pomalowane.user.UserMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class AppointmentService {

    private UserMapper userMapper;
    private UserDao userDao;
    private ClientDao clientDao;
    private ClientMapper clientMapper;
    private WorkDao workDao;
    private AppointmentDao appointmentDao;
    private AppointmentDetailsMapper appointmentDetailsMapper;
    private FromDtoService fromDtoService;

    public Appointment createAppointment(CreateAppointmentRequest createAppointmentRequest) throws Exception {
        Client client = clientDao.findById(createAppointmentRequest.getClientId())
                .orElseThrow(Exception::new);
        User employee = userDao.findById(createAppointmentRequest.getEmployeeId())
                .orElseThrow(Exception::new);

        Appointment appointment = Appointment.builder()
                .startDate(createAppointmentRequest.getStartDate())
                .client(client)
                .employee(employee)
                .build();

        List<AppointmentDetails> appointmentDetailsList = createAndSaveAppointmentDetails(createAppointmentRequest, appointment);
        calculateAndSetFinishDate(appointment, appointmentDetailsList);
        calculateAndSetWorksSum(appointment, appointmentDetailsList);

        appointment.setAppointmentDetails(appointmentDetailsList);

        return appointmentDao.save(appointment);
    }

    private List<AppointmentDetails> createAndSaveAppointmentDetails(CreateAppointmentRequest createAppointmentRequest,
                                                                     Appointment appointment) throws Exception {
        List<AppointmentDetails> appointmentDetailsList = new ArrayList<>();

        for (Long workId : createAppointmentRequest.getWorkIds()) {
            Work work = workDao.findById(workId)
                    .orElseThrow(Exception::new);

            AppointmentDetails appointmentDetails = AppointmentDetails.builder()
                    .appointment(appointment)
                    .work(work)
                    .build();
            appointmentDetailsList.add(appointmentDetails);
        }

        return appointmentDetailsList;
    }

    private Appointment calculateAndSetFinishDate(Appointment appointment, List<AppointmentDetails> appointmentDetailsList) {
        int hoursSum = 0;
        int minutesSum = 0;
        for (AppointmentDetails appointmentDetails : appointmentDetailsList) {
            hoursSum += appointmentDetails.getWork().getHoursDuration();
            minutesSum += appointmentDetails.getWork().getMinutesDuration();
        }

        LocalDateTime startDate = appointment.getStartDate();
        LocalDateTime finishDate = startDate.plusHours(hoursSum).plusMinutes(minutesSum);
        appointment.setFinishDate(finishDate);

        return appointment;
    }

    private void calculateAndSetWorksSum(Appointment appointment, List<AppointmentDetails> appointmentDetailsList) throws Exception {
        BigDecimal totalSum = BigDecimal.ZERO;
        for (AppointmentDetails appointmentDetails : appointmentDetailsList) {
            Work work = workDao.findById(appointmentDetails.getWork().getId())
                    .orElseThrow(Exception::new);
            totalSum = totalSum.add(work.getPrice());
        }
        appointment.setWorksSum(totalSum);
    }

}