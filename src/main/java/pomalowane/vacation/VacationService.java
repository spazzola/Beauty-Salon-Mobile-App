package pomalowane.vacation;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import pomalowane.appointment.Appointment;
import pomalowane.mappers.FromDtoService;
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

    @Transactional
    public Vacation createVacation(CreateVacationRequest createVacationRequest) {
        User employee = userDao.findById(createVacationRequest.getEmployeeId()).get();

        Vacation vacation = Vacation.builder()
                .startDate(createVacationRequest.getStartDate())
                .finishDate(createVacationRequest.getFinishDate())
                .employee(employee)
                .build();

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

}