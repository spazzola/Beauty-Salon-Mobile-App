package pomalowane.work;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

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

}