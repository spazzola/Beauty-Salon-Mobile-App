package pomalowane.service;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pomalowane.appointment.appointmentdetails.AppointmentDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "services")
public class Service {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<AppointmentDetails> appointmentDetails;

}