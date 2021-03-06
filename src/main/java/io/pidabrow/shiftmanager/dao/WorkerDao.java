package io.pidabrow.shiftmanager.dao;

import io.pidabrow.shiftmanager.domain.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkerDao extends JpaRepository<Worker, Long> {

    @Query("SELECT DISTINCT w FROM Worker w " +
            "LEFT JOIN FETCH w.shiftAssignments sa " +
            "WHERE w.id = :id")
    Optional<Worker> findWorkerByIdEagerly(Long id);
}
