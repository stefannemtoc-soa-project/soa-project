package ro.stefannemtoc.soaproject.models.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_logger")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventLogger {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "time_of_event", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "event_sender")
    private String eventSender;

    public EventLogger(LocalDateTime dateTime, String eventSender) {
        this.dateTime = dateTime;
        this.eventSender = eventSender;
    }
}
