package pomalowane.cost;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CostMapper {

    public CostDto toDto(Cost cost) {
        return CostDto.builder()
                .id(cost.getId())
                .name(cost.getName())
                .value(cost.getValue())
                .addedDate(cost.getAddedDate())
                .build();
    }

    public List<CostDto> toDto(List<Cost> costs) {
        return costs.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Cost fromDto(CostDto costDto) {
        return Cost.builder()
                .id(costDto.getId())
                .name(costDto.getName())
                .value(costDto.getValue())
                .addedDate(costDto.getAddedDate())
                .build();
    }

    public List<Cost> fromDto(List<CostDto> costsDto) {
        return costsDto.stream()
                .map(this::fromDto)
                .collect(Collectors.toList());
    }

}