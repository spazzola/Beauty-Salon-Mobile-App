package pomalowane.vacation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVacationRequest extends CreateVacationRequest {

    private Long id;

}