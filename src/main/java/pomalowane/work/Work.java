package pomalowane.work;

import com.sun.istack.NotNull;
import lombok.*;
import pomalowane.appointment.appointmentdetails.AppointmentDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
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

    @Transient
    private BigDecimal providedPrice;

    @NotNull
    private int hoursDuration;

    @NotNull
    private int minutesDuration;

    private String iconName;

    private boolean isVisible;

    @OneToMany(mappedBy = "work")
    private List<AppointmentDetails> appointmentDetails;

    @Override
    public boolean equals(Object o) {
        Work work = (Work) o;
        return this.getId().equals(work.getId());
    }

    @Override
    public String toString() {
        return "Work{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", providedPrice=" + providedPrice +
                ", hoursDuration=" + hoursDuration +
                ", minutesDuration=" + minutesDuration +
                ", iconName='" + iconName + '\'' +
                ", isVisible=" + isVisible +
                '}';
    }

}