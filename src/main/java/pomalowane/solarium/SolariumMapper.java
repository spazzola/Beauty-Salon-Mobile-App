package pomalowane.solarium;

import org.springframework.stereotype.Component;

@Component
public class SolariumMapper {

    public SolariumDto toDto(Solarium solarium) {
        return SolariumDto.builder()
                .usedDate(solarium.getUsedDate())
                .usedTime(solarium.getUsedTime())
                .build();
    }

}