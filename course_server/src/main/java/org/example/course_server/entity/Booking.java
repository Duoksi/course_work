package org.example.course_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int spotNumber;  // Номер места

    private String tcName;  // Название ТЦ

    @Column(columnDefinition = "TIMESTAMP(0)")
    @CreationTimestamp
    private LocalDateTime bookingTime;

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime startTime;

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime endTime;

    private String status;
}

