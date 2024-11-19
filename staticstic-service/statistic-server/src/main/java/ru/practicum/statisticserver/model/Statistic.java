package ru.practicum.statisticserver.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "statistic")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "app")
    private String app;
    @Column(name = "uri")
    private String uri;
    @Column(name = "ip")
    private String ip;
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
