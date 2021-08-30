package pomalowane.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {


    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String mail;

}