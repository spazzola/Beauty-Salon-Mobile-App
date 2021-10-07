package pomalowane.sms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.*;
import pomalowane.appointment.Appointment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "smses")
public class Sms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sms_id")
    private Long id;

    private String providedId;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "appointment_fk")
    private Appointment appointment;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime sendDate;


    @Override
    public String toString() {
        return "Sms{" +
                "id=" + id +
                ", providedId='" + providedId + '\'' +
                ", sendDate=" + sendDate +
                '}';
    }

}