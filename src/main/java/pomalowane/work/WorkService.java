package pomalowane.work;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Data
@Service
public class WorkService {

    private WorkDao workDao;


    public Work createWork(CreateWorkRequest createWorkRequest) {
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

    public void deleteWork(Long id) {
        workDao.deleteById(id);
    }

    public List<Work> getAll() {
        return workDao.findAll();
    }

}