package pomalowane.work;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Data
@Service
public class WorkService {

    private WorkDao workDao;


    public Work createWork(CreateWorkRequest createWorkRequest) {
        validateWork(createWorkRequest);
        Work work = Work.builder()
                .name(createWorkRequest.getName())
                .price(createWorkRequest.getPrice())
                .hoursDuration(createWorkRequest.getHoursDuration())
                .minutesDuration(createWorkRequest.getMinutesDuration())
                .build();

        return workDao.save(work);
    }

    @Transactional
    public Work updateWork(WorkDto workDto) {
        Work work = workDao.getById(workDto.getId());
        work.setName(workDto.getName());
        work.setPrice(workDto.getPrice());
        work.setHoursDuration(workDto.getHoursDuration());
        work.setMinutesDuration(workDto.getMinutesDuration());

        return workDao.save(work);
    }

    @Transactional
    public void deleteWork(Long id) {
        workDao.deleteById(id);
    }

    @Transactional
    public List<Work> getAll() {
        return workDao.findAll();
    }

    private void validateWork(CreateWorkRequest createWorkRequest) {
        if (createWorkRequest.getName() == null || createWorkRequest.getName().equals("")) {
            throw new IllegalArgumentException("Bad value of Work's name: " + createWorkRequest.getName());
        }
        if (createWorkRequest.getPrice() == null || createWorkRequest.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Bad value of Work's price: " + createWorkRequest.getPrice());
        }
        if (createWorkRequest.getHoursDuration() < 0) {
            throw new IllegalArgumentException("Bad value of Work's hoursDuration: " + createWorkRequest.getHoursDuration());
        }
        if (createWorkRequest.getMinutesDuration() < 0) {
            throw new IllegalArgumentException("Bad value of Work's minutesDuration: " + createWorkRequest.getMinutesDuration());
        }
        if (createWorkRequest.getHoursDuration() <= 0 && createWorkRequest.getMinutesDuration() <= 0) {
            throw new IllegalArgumentException("Bad value of Work's hoursDuration: " + createWorkRequest.getHoursDuration() +
                    ", and minutesDuration: " + createWorkRequest.getMinutesDuration());
        }
    }

}