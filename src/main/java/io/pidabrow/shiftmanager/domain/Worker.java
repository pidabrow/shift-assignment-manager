package io.pidabrow.shiftmanager.domain;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Worker {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Setter
    private String fullName;

    @Setter
    private String phoneNumber;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="worker", orphanRemoval = true)
    private Set<ShiftAssignment> shiftAssignments;

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
