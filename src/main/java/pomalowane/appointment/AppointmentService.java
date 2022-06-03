package pomalowane.appointment;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pomalowane.appointment.appointmentdetails.AppointmentDetails;
import pomalowane.appointment.appointmentdetails.AppointmentDetailsDao;
import pomalowane.client.Client;
import pomalowane.client.ClientDao;
import pomalowane.mappers.FromDtoService;
import pomalowane.mappers.ToDtoService;
import pomalowane.sms.SmsDao;
import pomalowane.sms.SmsService;
import pomalowane.user.UserDao;
import pomalowane.vacation.Vacation;
import pomalowane.vacation.VacationService;
import pomalowane.work.CreateWorkRequest;
import pomalowane.work.Work;
import pomalowane.work.WorkDao;
import pomalowane.user.User;
import pomalowane.work.WorkDto;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor
@Service
public class AppointmentService {

    private UserDao userDao;
    private ClientDao clientDao;
    private WorkDao workDao;
    private AppointmentDao appointmentDao;
    private AppointmentDetailsDao appointmentDetailsDao;
    private SmsService smsService;
    private FromDtoService fromDtoService;
    private ToDtoService toDtoService;
    private SmsDao smsDao;
    private VacationService vacationService;
    private static final Logger logger = LogManager.getLogger(AppointmentService.class);


    @Transactional
    public Appointment createAppointment(CreateAppointmentRequest createAppointmentRequest) throws Exception {
        validateAppointment(createAppointmentRequest);

        Client client = clientDao.findById(createAppointmentRequest.getClientId())
                .orElseThrow(Exception::new);
        User employee = userDao.findById(createAppointmentRequest.getEmployeeId())
                .orElseThrow(Exception::new);

        Appointment appointment = Appointment.builder()
                .startDate(createAppointmentRequest.getStartDate())
                .client(client)
                .employee(employee)
                .percentageValueToAdd(createAppointmentRequest.getPercentageValueToAdd())
                .note(createAppointmentRequest.getNote())
                .build();

        List<AppointmentDetails> appointmentDetailsList = createAndSaveAppointmentDetails(createAppointmentRequest.getWorks(), appointment);
        List<Long> workIds = createAppointmentRequest.getWorks()
                .stream()
                .map(WorkDto::getId)
                .collect(Collectors.toList());
        LocalDateTime finishDate = calculateFinishDate(appointment.getStartDate(), workIds);

        checkIfCollidesWithVacation(employee.getId(), createAppointmentRequest.getStartDate(), finishDate);

        appointment.setFinishDate(finishDate);
        calculateAndSetWorksSum(appointment, appointmentDetailsList);

        appointment.setAppointmentDetails(appointmentDetailsList);

        //smsService.setSmsReminder(appointment);

        return appointmentDao.save(appointment);

    }

    @Transactional
    public Appointment updateAppointment(UpdateAppointmentRequest updateAppointmentRequest) throws Exception {
        Appointment appointment = appointmentDao.getById(updateAppointmentRequest.getAppointmentId());
        LocalDateTime startDate = updateAppointmentRequest.getStartDate();

        int month = startDate.getMonth().getValue();
        int year = startDate.getYear();
        List<Appointment> appointments = appointmentDao.getUserMonthAppointments(month, year, updateAppointmentRequest.getEmployeeId());
        appointments.removeIf(searchedAppointment -> searchedAppointment.getId().equals(updateAppointmentRequest.getAppointmentId()));

        List<Long> workIds = getWorkIdsFromRequest(updateAppointmentRequest);
        LocalDateTime finishDate = calculateFinishDate(updateAppointmentRequest.getStartDate(), workIds);

        checkIfCollidesWithVacation(updateAppointmentRequest.getEmployeeId(), updateAppointmentRequest.getStartDate(), finishDate);

        validateDate(appointments, updateAppointmentRequest.getStartDate(), finishDate);

        logger.info("Wizyta przed aktualizacja: " + appointment);

        Client clientId = clientDao.getById(updateAppointmentRequest.getClientId());
        User employeeId = userDao.getById(updateAppointmentRequest.getEmployeeId());

        appointment.setClient(clientId);
        appointment.setEmployee(employeeId);
        appointment.setNote(updateAppointmentRequest.getNote());
        appointment.setStartDate(updateAppointmentRequest.getStartDate());
        appointment.setPercentageValueToAdd(updateAppointmentRequest.getPercentageValueToAdd());

        List<AppointmentDetails> appointmentDetailsList = appointment.getAppointmentDetails();

        if (appointmentDetailsList.size() == updateAppointmentRequest.getWorks().size()) {
            reassignWorksToAppointmentDetails(appointment, updateAppointmentRequest);
        }

        if (appointmentDetailsList.size() > updateAppointmentRequest.getWorks().size()) {
            deleteAppointmentDetails(appointment, updateAppointmentRequest);
        }

        if (appointmentDetailsList.size() < updateAppointmentRequest.getWorks().size()) {
            createAndSaveAppointmentDetails(appointment, updateAppointmentRequest);
        }

        appointmentDetailsList = assignProvidedPriceToWorks(appointmentDetailsList, updateAppointmentRequest);

        workIds = getWorkIdsFromAppointment(appointment);
        finishDate = calculateFinishDate(updateAppointmentRequest.getStartDate(), workIds);
        appointment.setFinishDate(finishDate);
        calculateAndSetWorksSum(appointment, appointmentDetailsList);

        //smsService.updateSmsReminder(appointment);

        return appointmentDao.save(appointment);
    }

    @Transactional
    public void deleteAppointment(Long id) {
        //smsDao.deleteByAppointmentId(id);
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

    @Transactional
    public List<Appointment> getAll() {
        return appointmentDao.findAll();
    }

    @Transactional
    public List<Appointment> getIncomingAppointments(Long clientId) {
        LocalDateTime now = LocalDateTime.now();
        return appointmentDao.findByStartDateGreaterThanAndClientId(now, clientId);
    }

    private void validateAppointment(CreateAppointmentRequest createAppointmentRequest) throws Exception {
        int month = createAppointmentRequest.getStartDate().getMonth().getValue();
        int year = createAppointmentRequest.getStartDate().getYear();
        List<Appointment> appointments = appointmentDao.getUserMonthAppointments(month, year, createAppointmentRequest.getEmployeeId());

        validateEmployee(createAppointmentRequest);
        validateClient(createAppointmentRequest);
        validateWorks(createAppointmentRequest);

        List<Long> workIds = getWorkIdsFromRequest(createAppointmentRequest);
        LocalDateTime finishDate = calculateFinishDate(createAppointmentRequest.getStartDate(), workIds);
        validateDate(appointments, createAppointmentRequest.getStartDate(), finishDate);
    }

    private void validateEmployee(CreateAppointmentRequest createAppointmentRequest) {
        validateId(createAppointmentRequest.getEmployeeId(), "Employee (User)");
    }

    private void validateClient(CreateAppointmentRequest createAppointmentRequest) {
        validateId(createAppointmentRequest.getClientId(), "Client");
    }

    private void validateWorks(CreateAppointmentRequest createAppointmentRequest) {
        if (createAppointmentRequest.getWorks() == null) {
            throw new IllegalArgumentException("Works list is null");
        }
    }

    private void validateId(Long id, String idType) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Bad id of " + idType + ": " + id);
        }
    }

    private LocalDateTime calculateFinishDate(LocalDateTime startDate, List<Long> workIds) throws Exception {
        int hoursSum = 0;
        int minutesSum = 0;

        for (Long workId : workIds) {
            Work work = workDao.findById(workId)
                    .orElseThrow(Exception::new);
            hoursSum += work.getHoursDuration();
            minutesSum += work.getMinutesDuration();
        }

        return startDate.plusHours(hoursSum).plusMinutes(minutesSum);
    }

    private void validateDate(List<Appointment> appointments, LocalDateTime startDate, LocalDateTime endDate) {
        checkIfCollidesWithAppointment(appointments, startDate, endDate);
    }

    private void checkIfCollidesWithAppointment(List<Appointment> appointments, LocalDateTime startDate, LocalDateTime endDate) {
        for (Appointment appointment : appointments) {
            if (startDate.isAfter(appointment.getStartDate()) && startDate.isBefore(appointment.getFinishDate())) {
                throw new IllegalArgumentException("The date collides with another appointment with an id: " + appointment.getId());
            }
            if (endDate.isAfter(appointment.getStartDate()) && endDate.isBefore(appointment.getFinishDate())) {
                throw new IllegalArgumentException("The date collides with another appointment with an id: " + appointment.getId());
            }
            if (startDate.isEqual(appointment.getStartDate())) {
                throw new IllegalArgumentException("The date collides with another appointment with an id: " + appointment.getId());
            }
            if (startDate.isBefore(appointment.getStartDate()) && endDate.isAfter(appointment.getStartDate())) {
                throw new IllegalArgumentException("The date collides with another appointment with an id: " + appointment.getId());
            }
        }
    }

    private void checkIfCollidesWithVacation(Long userId, LocalDateTime startDate, LocalDateTime finishDate) {
        Optional<List<Vacation>> optionalVacationsList = vacationService.getDayVacations(userId,
                                                                                startDate.getDayOfMonth(),
                                                                                startDate.getMonthValue(),
                                                                                startDate.getYear());
        if (optionalVacationsList.isPresent()) {
            List<Vacation> vacations = optionalVacationsList.get();

            vacations.forEach(vacation -> {
                String exceptionMessage = "An Appointment's date collides with Vacation's date: \n" +
                        "Vacation: " + vacation + "\n " +
                        "Appointment's startDate: " + startDate + "\n" +
                        "Appointment's finishDate: " + finishDate;

                if (startDate.isEqual(vacation.getStartDate())) {
                    throw new IllegalArgumentException(exceptionMessage);
                }
                if (startDate.isAfter(vacation.getStartDate()) && startDate.isBefore(vacation.getFinishDate())) {
                    throw new IllegalArgumentException(exceptionMessage);
                }
                if (startDate.isBefore(vacation.getStartDate()) && finishDate.isAfter(vacation.getStartDate())) {
                    throw new IllegalArgumentException(exceptionMessage);
                }
                if (startDate.isBefore(vacation.getFinishDate()) && finishDate.isAfter(vacation.getFinishDate())) {
                    throw new IllegalArgumentException(exceptionMessage);
                }
            });
        }
    }

    private List<AppointmentDetails> createAndSaveAppointmentDetails(List<WorkDto> works,
                                                                     Appointment appointment) throws Exception {
        List<AppointmentDetails> appointmentDetailsList = createOrGetAppointmentDetailsList(appointment);
        for (WorkDto workDto : works) {
            Work work = workDao.findById(workDto.getId())
                    .orElseThrow(Exception::new);
            work.setProvidedPrice(workDto.getProvidedPrice());

            AppointmentDetails appointmentDetails = AppointmentDetails.builder()
                    .appointment(appointment)
                    .work(work)
                    .build();
            appointmentDetailsList.add(appointmentDetails);
        }

        return appointmentDetailsList;
    }

    private void createAndSaveAppointmentDetails(Appointment appointment, UpdateAppointmentRequest updateAppointmentRequest) throws Exception {
        List<Work> appointmentWorks = appointment.getAppointmentDetails()
                .stream()
                .map(AppointmentDetails::getWork)
                .collect(Collectors.toList());

        List<Work> requestWork = updateAppointmentRequest.getWorks()
                .stream()
                .map(workDto -> fromDtoService.workFromDto(workDto))
                .collect(Collectors.toList());

        List<WorkDto> worksToAdd = getDifferencesFromLists(requestWork, appointmentWorks)
                .stream()
                .map(work -> toDtoService.workToDto(work))
                .collect(Collectors.toList());

        List<AppointmentDetails> appointmentDetailsList = createAndSaveAppointmentDetails(worksToAdd, appointment);
        appointment.setAppointmentDetails(appointmentDetailsList);
    }

    private List<AppointmentDetails> createOrGetAppointmentDetailsList(Appointment appointment) {
        if (appointment.getAppointmentDetails() == null) {
            return new ArrayList<>();
        } else {
            return appointment.getAppointmentDetails();
        }
    }

    private void calculateAndSetWorksSum(Appointment appointment, List<AppointmentDetails> appointmentDetailsList) throws Exception {
        BigDecimal totalSum = BigDecimal.ZERO;
        for (AppointmentDetails appointmentDetails : appointmentDetailsList) {
            Work work = appointmentDetails.getWork();

            if (work.getProvidedPrice() != null && work.getProvidedPrice().compareTo(work.getPrice()) != 0) {
                totalSum = totalSum.add(work.getProvidedPrice());
            } else {
                totalSum = totalSum.add(work.getPrice());
            }
        }
        totalSum = addExtraAmount(appointment, totalSum);
        appointment.setWorksSum(totalSum);
    }

    private BigDecimal addExtraAmount(Appointment appointment, BigDecimal totalSum) {
        totalSum = addExtraAmountIfSunday(appointment, totalSum);
        double percentageValueToAdd = appointment.getPercentageValueToAdd();
        BigDecimal extraAmountToAdd = totalSum.multiply(BigDecimal.valueOf(percentageValueToAdd / 100));

        return totalSum.add(extraAmountToAdd);
    }

    private BigDecimal addExtraAmountIfSunday(Appointment appointment, BigDecimal worksValue) {
        if (appointment.getStartDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
            return worksValue.multiply(BigDecimal.valueOf(2));
            //BigDecimal amountToAdd = worksValue.multiply(BigDecimal.valueOf(2));
            //return worksValue.add(amountToAdd);
        } else {
            return worksValue;
        }
    }

    private void reassignWorksToAppointmentDetails(Appointment appointment, UpdateAppointmentRequest updateAppointmentRequest) {
        for (int i = 0; i < updateAppointmentRequest.getWorks().size(); i++) {
            Work work = workDao.getById(updateAppointmentRequest.getWorks().get(i).getId());
            AppointmentDetails appointmentDetails = appointment.getAppointmentDetails().get(i);
            appointmentDetails.setWork(work);
            appointmentDetailsDao.save(appointmentDetails);
        }
    }

    private void deleteAppointmentDetails(Appointment appointment, UpdateAppointmentRequest updateAppointmentRequest) {
        List<Work> appointmentWorks = appointment.getAppointmentDetails()
                .stream()
                .map(AppointmentDetails::getWork)
                .collect(Collectors.toList());

        List<Work> requestWork = updateAppointmentRequest.getWorks()
                .stream()
                .map(workDto -> fromDtoService.workFromDto(workDto))
                .collect(Collectors.toList());

        List<Work> worksToDelete = getDifferencesFromLists(appointmentWorks, requestWork);

        deleteAppointmentDetailsFromAppointment(worksToDelete, appointment);
        deleteAppointmentDetailsFromDb(worksToDelete);
    }

    private List<Long> getWorkIdsFromAppointment(Appointment appointment) {
        return appointment.getAppointmentDetails()
                .stream()
                .map(appointmentDetails -> appointmentDetails.getWork().getId())
                .collect(Collectors.toList());
    }

    private List<Long> getWorkIdsFromRequest(CreateAppointmentRequest createAppointmentRequest) {
        return createAppointmentRequest.getWorks()
                .stream()
                .map(WorkDto::getId)
                .collect(Collectors.toList());
    }

    private List<Work> getDifferencesFromLists(List<Work> firstList, List<Work> secondList) {
        return firstList.stream()
                .filter(el -> !secondList.contains(el))
                .collect(Collectors.toList());
    }

    private void deleteAppointmentDetailsFromAppointment(List<Work> worksToDelete, Appointment appointment) {
        worksToDelete
                .forEach(work -> appointment.getAppointmentDetails()
                        .removeIf(appointmentDetails -> appointmentDetails.getWork().getId().equals(work.getId())));
    }

    private void deleteAppointmentDetailsFromDb(List<Work> worksToDelete) {
        worksToDelete.forEach(work -> appointmentDetailsDao.deleteByWorkId(work.getId()));
    }

    private List<AppointmentDetails> assignProvidedPriceToWorks(List<AppointmentDetails> appointmentDetails,
                                                                UpdateAppointmentRequest updateAppointmentRequest) {
        List<AppointmentDetails> sortedAppointmentDetails = appointmentDetails
                .stream()
                .sorted(Comparator.comparing(el -> el.getWork().getId()))
                .collect(Collectors.toList());

        List<WorkDto> sortedWorks = updateAppointmentRequest.getWorks()
                .stream()
                .sorted(Comparator.comparing(WorkDto::getId))
                .collect(Collectors.toList());

        for (int i = 0; i < sortedAppointmentDetails.size(); i++) {
            Work work = sortedAppointmentDetails.get(i).getWork();
            work.setProvidedPrice(sortedWorks.get(i).getProvidedPrice());
        }

        return sortedAppointmentDetails;
    }

    private boolean checkIfAdmin(Long userId) throws Exception {
        User user = userDao.findById(userId)
                .orElseThrow(Exception::new);

        return user.getRole().equals("ADMIN");
    }

}