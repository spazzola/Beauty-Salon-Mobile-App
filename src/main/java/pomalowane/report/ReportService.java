package pomalowane.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pomalowane.appointment.Appointment;
import pomalowane.appointment.AppointmentDao;
import pomalowane.appointment.appointmentdetails.AppointmentDetails;
import pomalowane.cost.Cost;
import pomalowane.cost.CostDao;
import pomalowane.mappers.ToDtoService;
import pomalowane.user.User;
import pomalowane.user.UserDao;
import pomalowane.user.UserDto;

@AllArgsConstructor
@Service
public class ReportService {

    private UserDao userDao;
    private CostDao costDao;
    private AppointmentDao appointmentDao;
    private ToDtoService toDtoService;

    @Transactional
    public Report generateMonthlyReport(int month, int year) {
        List<Appointment> appointments = appointmentDao.getMonthAppointments(month, year);
        BigDecimal totalWorkSum = calculateTotalWorkSum(appointments);
        BigDecimal totalCostValue = calculateTotalCostsValue(month, year);
        BigDecimal income = totalWorkSum.subtract(totalCostValue);

        List<User> users = userDao.findAll();
        for (User user : users) {
            int hours = 0;
            int minutes = 0;
            for (Appointment appointment : appointments) {
                if (appointment.getEmployee().getId().equals(user.getId())) {
                    List<AppointmentDetails> appointmentDetailsList = appointment.getAppointmentDetails();
                    for (AppointmentDetails appointmentDetails : appointmentDetailsList) {
                       hours += appointmentDetails.getWork().getHoursDuration();
                       minutes += appointmentDetails.getWork().getMinutesDuration();
                    }
                }
            }
            int workedHours = calculateTime(hours, minutes);
            user.setWorkedHours(workedHours);
        }
        List<UserDto> usersDto = toDtoService.userToDto(users);

        LocalDateTime date = LocalDateTime.of(year, month, 1, 1, 1);
        return Report.builder()
                .date(date)
                .totalWorkSum(totalWorkSum)
                .totalCostsValue(totalCostValue)
                .income(income)
                .users(usersDto)
                .build();
    }

    @Transactional
    public Report generateYearlyReport(int year) {
        List<Appointment> appointments = appointmentDao.getYearAppointments(year);
        BigDecimal totalWorkSum = calculateTotalWorkSum(appointments);
        BigDecimal totalCostValue = calculateTotalYearCostsValue(year);
        BigDecimal income = totalWorkSum.subtract(totalCostValue);

        List<User> users = calculateUsersWorkedHours(appointments);
        List<UserDto> usersDto = toDtoService.userToDto(users);

        LocalDateTime date = LocalDateTime.of(year, 12, 1, 1, 1);
        return Report.builder()
                .date(date)
                .totalWorkSum(totalWorkSum)
                .totalCostsValue(totalCostValue)
                .income(income)
                .users(usersDto)
                .build();
    }

    private BigDecimal calculateTotalWorkSum(List<Appointment> appointments) {
        BigDecimal totalWorkSum = BigDecimal.ZERO;
        for (Appointment appointment : appointments) {
            totalWorkSum = totalWorkSum.add(appointment.getWorksSum());
        }
        return totalWorkSum;
    }

    private BigDecimal calculateTotalCostsValue(int month, int year) {
        List<Cost> costs = costDao.getMonthCosts(month, year);
        BigDecimal totalCostsValue = BigDecimal.ZERO;
        for (Cost cost : costs) {
            totalCostsValue = totalCostsValue.add(cost.getValue());
        }
        return totalCostsValue;
    }

    private BigDecimal calculateTotalYearCostsValue(int year) {
        List<Cost> costs = costDao.getYearCosts(year);
        BigDecimal totalCostsValue = BigDecimal.ZERO;
        for (Cost cost : costs) {
            totalCostsValue = totalCostsValue.add(cost.getValue());
        }
        return totalCostsValue;
    }

    public int calculateTime(int hours, int minutes) {
        int totalTime = hours * 60;
        totalTime += minutes;
        hours = totalTime / 60;

        return hours;
    }

    private List<User> calculateUsersWorkedHours(List<Appointment> appointments) {
        List<User> users = userDao.findAll();
        for (User user : users) {
            int hours = 0;
            int minutes = 0;
            for (Appointment appointment : appointments) {
                if (appointment.getEmployee().getId().equals(user.getId())) {
                    List<AppointmentDetails> appointmentDetailsList = appointment.getAppointmentDetails();
                    for (AppointmentDetails appointmentDetails : appointmentDetailsList) {
                        hours += appointmentDetails.getWork().getHoursDuration();
                        minutes += appointmentDetails.getWork().getMinutesDuration();
                    }
                }
            }
            int workedHours = calculateTime(hours, minutes);
            user.setWorkedHours(workedHours);
        }
        return users;
    }

}