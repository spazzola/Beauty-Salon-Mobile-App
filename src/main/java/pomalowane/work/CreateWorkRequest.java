package pomalowane.work;

import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkRequest {

    private String name;
    private BigDecimal price;
    private int hoursDuration;
    private int minutesDuration;
    private Long iconId;

}