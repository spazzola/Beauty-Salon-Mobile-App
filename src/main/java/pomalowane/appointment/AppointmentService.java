package pomalowane.appointment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pomalowane.appointment.appointmentdetails.AppointmentDetails;
import pomalowane.appointment.appointmentdetails.AppointmentDetailsDao;
import pomalowane.client.Client;
import pomalowane.client.ClientDao;
import pomalowane.user.UserDao;
import pomalowane.work.Work;
import pomalowane.work.WorkDao;
import pomalowane.user.User;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AppointmentService {

    private UserDao userDao;
    private ClientDao clientDao;
    private WorkDao workDao;
    private AppointmentDao appointmentDao;
    private AppointmentDetailsDao appointmentDetailsDao;


    public Appointment createAppointment(CreateAppointmentRequest createAppointmentRequest) throws Exception {
        Client client = clientDao.findById(createAppointmentRequest.getClientId())
                .orElseThrow(Exception::new);
        User employee = userDao.findById(createAppointmentRequest.getEmployeeId())
                .orElseThrow(Exception::new);

        Appointment appointment = Appointment.builder()
                .startDate(createAppointmentRequest.getStartDate())
                .client(client)
                .employee(employee)
                .percentageValueToAdd(createAppointmentRequest.getPercentageValueToAdd())
                .build();

        List<AppointmentDetails> appointmentDetailsList = createAndSaveAppointmentDetails(createAppointmentRequest.getWorkIds(), appointment);
        calculateAndSetFinishDate(appointment, appointmentDetailsList);
        calculateAndSetWorksSum(appointment, appointmentDetailsList);

        appointment.setAppointmentDetails(appointmentDetailsList);

        return appointmentDao.save(appointment);
    }

    @Transactional
    public Appointment updateAppointment(UpdateAppointmentRequest updateAppointmentRequest) throws Exception {
        Appointment appointment = appointmentDao.getById(updateAppointmentRequest.getAppointmentId());

        Client client = clientDao.getById(updateAppointmentRequest.getClientId());
        User employee = userDao.getById(updateAppointmentRequest.getEmployeeId());

        appointment.setClient(client);
        appointment.setEmployee(employee);
        appointment.setPercentageValueToAdd(updateAppointmentRequest.getPercentageValueToAdd());
        appointment.setStartDate(updateAppointmentRequest.getStartDate());

        List<AppointmentDetails> appointmentDetailsList = appointment.getAppointmentDetails();
        if (appointmentDetailsList.size() == updateAppointmentRequest.getWorkIds().size()) {
            reassignWorksToAppointmentDetails(appointment, updateAppointmentRequest);
            calculateAndSetFinishDate(appointment, appointmentDetailsList);
            calculateAndSetWorksSum(appointment, appointmentDetailsList);
        }

        if (appointmentDetailsList.size() > updateAppointmentRequest.getWorkIds().size()) {
            deleteAppointmentDetails(appointment, updateAppointmentRequest);
            calculateAndSetFinishDate(appointment, appointmentDetailsList);
            calculateAndSetWorksSum(appointment, appointmentDetailsList);
        }

        if (appointmentDetailsList.size() < updateAppointmentRequest.getWorkIds().size()) {
            createAndSaveAppointmentDetails(appointment, updateAppointmentRequest);
            calculateAndSetFinishDate(appointment, appointmentDetailsList);
            calculateAndSetWorksSum(appointment, appointmentDetailsList);
        }

        return appointmentDao.save(appointment);
    }

    public List<Appointment> getMonthAppointments(int month, int year, Long userId) throws Exception {
        List<Appointment> appointments;
        if (checkIfAdmin(userId)) {
            appointments = appointmentDao.getMonthAppointments(month, year);
        } else {
            appointments = appointmentDao.getUserMonthAppointments(month, year, userId);
        }
        return appointments;
    }

    public List<Appointment> getAll() {
        return appointmentDao.findAll();
    }

    private List<AppointmentDetails> createAndSaveAppointmentDetails(List<Long> workIds,
                                                                     Appointment appointment) throws Exception {
        List<AppointmentDetails> appointmentDetailsList = createOrGetAppointmentDetailsList(appointment);
        for (Long workId : workIds) {
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

    private void createAndSaveAppointmentDetails(Appointment appointment, UpdateAppointmentRequest updateAppointmentRequest) throws Exception {
        List<Long> workIds = getWorksAppointmentDetailsIds(appointment);
        List<Long> workIdsToAdd = getDifferencesFromLists(updateAppointmentRequest.getWorkIds(), workIds);
        List<AppointmentDetails> appointmentDetailsList = createAndSaveAppointmentDetails(workIdsToAdd, appointment);
        appointment.setAppointmentDetails(appointmentDetailsList);
    }

    private List<AppointmentDetails> createOrGetAppointmentDetailsList(Appointment appointment) {
        if (appointment.getAppointmentDetails() == null) {
            return new ArrayList<>();
        } else {
            return appointment.getAppointmentDetails();
        }
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

        totalSum = addExtraAmountIfSunday(appointment, totalSum);
        appointment.setWorksSum(totalSum);
    }

    private BigDecimal addExtraAmountIfSunday(Appointment appointment, BigDecimal worksValue) {
        if (appointment.getStartDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
            BigDecimal amountToAdd = worksValue.multiply(BigDecimal.valueOf(appointment.getPercentageValueToAdd()));
            return worksValue.add(amountToAdd);
        } else {
            return worksValue;
        }
    }

    private boolean checkIfAdmin(Long userId) throws Exception {
        User user = userDao.findById(userId)
                .orElseThrow(Exception::new);

        return user.getRole().equals("ADMIN");
    }

    private void reassignWorksToAppointmentDetails(Appointment appointment, UpdateAppointmentRequest updateAppointmentRequest) {
        for (int i = 0; i < updateAppointmentRequest.getWorkIds().size(); i++) {
            Work work = workDao.getById(updateAppointmentRequest.getWorkIds().get(i));
            AppointmentDetails appointmentDetails = appointment.getAppointmentDetails().get(i);
            appointmentDetails.setWork(work);
            appointmentDetailsDao.save(appointmentDetails);
        }
    }

    private void deleteAppointmentDetails(Appointment appointment, UpdateAppointmentRequest updateAppointmentRequest) {
        List<Long> workIds = getWorksAppointmentDetailsIds(appointment);
        List<Long> workIdsToDelete = getDifferencesFromLists(workIds, updateAppointmentRequest.getWorkIds());
        deleteAppointmentDetailsFromAppointment(workIdsToDelete, appointment);
        deleteAppointmentDetailsFromDb(workIdsToDelete);
    }

    private List<Long> getWorksAppointmentDetailsIds(Appointment appointment) {
        List<Long> ids = new ArrayList<>();
        for (AppointmentDetails appointmentDetails : appointment.getAppointmentDetails()) {
            ids.add(appointmentDetails.getWork().getId());
        }
        return ids;
    }

    private void deleteAppointmentDetailsFromAppointment(List<Long> workIdsToDelete, Appointment appointment) {
        workIdsToDelete.forEach(workId -> appointment.getAppointmentDetails()
                .removeIf(appointmentDetails -> appointmentDetails.getWork().getId().equals(workId)));
    }

    private void deleteAppointmentDetailsFromDb(List<Long> workIdsToDelete) {
        workIdsToDelete.forEach(workId -> appointmentDetailsDao.deleteByWorkId(workId));
    }

    private List<Long> getDifferencesFromLists(List<Long> firstList, List<Long> secondList) {
        return firstList.stream()
                .filter(el -> !secondList.contains(el))
                .collect(Collectors.toList());
    }

}