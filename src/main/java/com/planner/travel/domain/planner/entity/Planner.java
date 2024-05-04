package com.planner.travel.domain.planner.entity;

import com.planner.travel.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Planner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean isDeleted;

    private boolean isPrivate;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

}
