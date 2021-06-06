package io.pidabrow.shiftmanager.dao;

import io.pidabrow.shiftmanager.domain.ShiftAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftAssignmentDao extends JpaRepository<ShiftAssignment, Long> {

    boolean existsShiftAssignmentByWorkerIdAndShiftDate(@Param("workerId") Long workerId, @Param("shiftDate") LocalDate shiftDate);

    Optional<ShiftAssignment> findShiftAssignmentByWorkerIdAndShiftDate(@Param("workerId") Long workerId, @Param("shiftDate") LocalDate shiftDate);

    List<ShiftAssignment> findAllByWorkerId(Long workerId);
}
