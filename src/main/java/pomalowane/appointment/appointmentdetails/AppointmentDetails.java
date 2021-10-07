package pomalowane.appointment.appointmentdetails;

import com.sun.istack.NotNull;
import lombok.*;
import pomalowane.appointment.Appointment;
import pomalowane.work.Work;

import javax.persistence.*;

@Entity
@Getter
@Setter
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
    @JoinColumn(name = "work_fk")
    private Work work;


    @Override
    public String toString() {
        return "AppointmentDetails{" +
                "id=" + id +
                ", work=" + work +
                '}';
    }
}