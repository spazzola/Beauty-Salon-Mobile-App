package pomalowane.appointment.appointmentdetails;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pomalowane.appointment.Appointment;
import pomalowane.work.Work;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointment_details")
public class AppointmentDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_details_id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "appointment_fk")
    private Appointment appointment;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "service_fk")
    private Work work;

}