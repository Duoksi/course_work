package org.example.course_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"spotNumber", "tcName"}))
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Внутренний уникальный ID для сущности

    private int spotNumber;  // Номер парковочного места в ТЦ

    private String tcName;
}
