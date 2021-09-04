package pomalowane.solarium;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@AllArgsConstructor
@Service
public class SolariumService {

    private SolariumDao solariumDao;


    public Solarium useSolarium(SolariumDto solariumDto) {
        int month = solariumDto.getUsedDate().getMonth().getValue();
        int year = solariumDto.getUsedDate().getYear();
        Solarium solarium = solariumDao.getMonthSolarium(month, year);

        if (solarium == null) {
            solarium = createNewSolarium(solariumDto);
        } else {
            solarium = increaseUsedTime(solarium, solariumDto);
        }
        return solariumDao.save(solarium);
    }

    private Solarium createNewSolarium(SolariumDto solariumDto) {
        return Solarium.builder()
                .usedDate(solariumDto.getUsedDate())
                .usedTime(solariumDto.getUsedTime())
                .build();
    }

    private Solarium increaseUsedTime(Solarium solarium, SolariumDto solariumDto) {
        int currentUsedTime = solarium.getUsedTime();
        currentUsedTime += solariumDto.getUsedTime();
        solarium.setUsedTime(currentUsedTime);

        return solarium;
    }

}