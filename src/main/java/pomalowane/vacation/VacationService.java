package pomalowane.vacation;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import pomalowane.user.User;
import pomalowane.user.UserDao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class VacationService {

    private VacationDao vacationDao;
    private UserDao userDao;

    private static final Logger logger = LogManager.getLogger(VacationService.class);

    @Transactional
    public Vacation createVacation(CreateVacationRequest createVacationRequest) {
        validateVacation(createVacationRequest.getStartDate(), createVacationRequest.getFinishDate(), createVacationRequest.getEmployeeId());

        User employee = userDao.findById(createVacationRequest.getEmployeeId()).get();

        Vacation vacation = Vacation.builder()
                .startDate(createVacationRequest.getStartDate())
                .finishDate(createVacationRequest.getFinishDate())
                .employee(employee)
                .build();

        return vacationDao.save(vacation);
    }

    @Transactional
    public Vacation updateVacation(UpdateVacationRequest updateVacationRequest) {
        validateVacation(updateVacationRequest.getStartDate(), updateVacationRequest.getFinishDate(), updateVacationRequest.getEmployeeId());

        Vacation vacation = vacationDao.getById(updateVacationRequest.getId());
        User employee = userDao.findById(updateVacationRequest.getEmployeeId()).get();

        logger.info("Urlop przed aktualizacja: " + vacation);

        vacation.setStartDate(updateVacationRequest.getStartDate());
        vacation.setFinishDate(updateVacationRequest.getFinishDate());
        vacation.setEmployee(employee);

        return vacationDao.save(vacation);
    }

    @Transactional
    public List<Vacation> getAllVacations() {
        return vacationDao.findAll();
    }

    @Transactional
    public Optional<List<Vacation>> getDayVacations(Long userId, int day, int month, int year) {
        return vacationDao.getVacationsByDate(userId, day, month, year);
    }

    @Transactional
    public void deleteVacation(Long id) {
        vacationDao.deleteById(id);
    }

    private void validateVacation(LocalDateTime startDate, LocalDateTime finishDate, Long employeeId) {
        if (finishDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Bad value of Vacation's finishDate (finishDate can't be before a startDate): " +
                    "startDate: " + startDate + "\nfinishDate: " + finishDate);
        }
        if (employeeId <= 0) {
            throw new IllegalArgumentException("Bad value of Employee's id: " + employeeId);
        }
    }

}