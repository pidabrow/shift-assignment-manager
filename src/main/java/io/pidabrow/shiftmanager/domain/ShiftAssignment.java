package io.pidabrow.shiftmanager.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ShiftAssignment {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @Setter
    @Enumerated(value = EnumType.STRING)
    private Shift shift;

    private LocalDate shiftDate;

    @Column(updatable = false)
    private ZonedDateTime dateCreated;
    private ZonedDateTime dateLastUpdated;

    @PrePersist
    public void onCreate() {
        this.dateCreated = ZonedDateTime.now();
        this.dateLastUpdated = ZonedDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.dateLastUpdated = ZonedDateTime.now();
    }
}
