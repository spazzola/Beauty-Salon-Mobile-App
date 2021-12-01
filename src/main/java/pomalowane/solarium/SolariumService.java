package pomalowane.solarium;


import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import pomalowane.user.UserController;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class SolariumService {

    private SolariumDao solariumDao;
    private static final Logger logger = LogManager.getLogger(SolariumService.class);

    @Transactional
    public Solarium useSolarium(SolariumDto solariumDto) {
        int month = solariumDto.getUsedDate().getMonth().getValue();
        int year = solariumDto.getUsedDate().getYear();
        Solarium solarium = solariumDao.getMonthSolarium(month, year);
        logger.info("Solarium przed uzyciem: " + solarium);
        if (solarium == null) {
            solarium = createNewSolarium(solariumDto);
        } else {
            increaseUsedTime(solarium, solariumDto);
        }
        return solariumDao.save(solarium);
    }

    @Transactional
    public Solarium getMonthSolarium(int month, int year) {
        if (solariumDao.getMonthSolarium(month, year) != null) {
            return solariumDao.getMonthSolarium(month, year);
        } else {
            return Solarium.builder()
                    .usedDate(LocalDateTime.of(year, month, 1, 0,0))
                    .usedTime(0)
                    .build();
        }
    }

    private Solarium createNewSolarium(SolariumDto solariumDto) {
        return Solarium.builder()
                .usedDate(solariumDto.getUsedDate())
                .usedTime(solariumDto.getUsedTime())
                .build();
    }

    private void increaseUsedTime(Solarium solarium, SolariumDto solariumDto) {
        int currentUsedTime = solarium.getUsedTime();
        currentUsedTime += solariumDto.getUsedTime();
        solarium.setUsedTime(currentUsedTime);

        //return solarium;
    }

}