package pomalowane.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.*;
import pomalowane.appointment.appointmentdetails.AppointmentDetails;
import pomalowane.client.Client;
import pomalowane.sms.Sms;
import pomalowane.user.User;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long id;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime startDate;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime finishDate;

    @NotNull
    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL)
    private List<AppointmentDetails> appointmentDetails;

    @OneToMany(mappedBy = "appointment")
    private List<Sms> smsReminders;

    private String note;

    private BigDecimal worksSum;

    @Transient
    private double percentageValueToAdd;

    @NotNull
    @OneToOne
    @JoinColumn(name = "client_fk")
    private Client client;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private User employee;


    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", finishDate=" + finishDate +
                ", appointmentDetails=" + appointmentDetails +
                ", smsReminders=" + smsReminders +
                ", note='" + note + '\'' +
                ", worksSum=" + worksSum +
                ", percentageValueToAdd=" + percentageValueToAdd +
                ", client=" + client +
                ", employee=" + employee +
                '}';
    }

}