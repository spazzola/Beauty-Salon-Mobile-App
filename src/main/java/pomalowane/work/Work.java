package pomalowane.work;

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
@Table(name = "works")
public class Work {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_id")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

    @NotNull
    private int hoursDuration;

    @NotNull
    private int minutesDuration;

//    //TODO to delete?
//    @OneToMany(mappedBy = "work", cascade = CascadeType.ALL)
//    private List<AppointmentDetails> appointmentDetails;

}