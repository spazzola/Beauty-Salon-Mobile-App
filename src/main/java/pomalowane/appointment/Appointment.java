package pomalowane.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pomalowane.appointment.appointmentdetails.AppointmentDetails;
import pomalowane.client.Client;
import pomalowane.user.User;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
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

    private String note;

    private BigDecimal worksSum;

    @Transient
    private double percentageValueToAdd;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_fk")
    private Client client;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User employee;

}