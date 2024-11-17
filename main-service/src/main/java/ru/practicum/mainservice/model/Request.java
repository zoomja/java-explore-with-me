package ru.practicum.mainservice.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.mainservice.model.enums.State;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    @Column(name = "create_date")
    private LocalDateTime created;
    @Enumerated(EnumType.STRING)
    private State status;

}
