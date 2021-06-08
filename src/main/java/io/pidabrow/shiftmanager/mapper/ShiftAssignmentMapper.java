package io.pidabrow.shiftmanager.mapper;

import io.pidabrow.shiftmanager.dao.WorkerDao;
import io.pidabrow.shiftmanager.domain.ShiftAssignment;
import io.pidabrow.shiftmanager.domain.Worker;
import io.pidabrow.shiftmanager.dto.ShiftAssignmentCreateDto;
import io.pidabrow.shiftmanager.dto.ShiftAssignmentDto;
import io.pidabrow.shiftmanager.exception.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShiftAssignmentMapper {

    private final WorkerDao workerDao;

    public ShiftAssignment toEntity(ShiftAssignmentCreateDto dto) {
        final Long workerId = dto.getWorkerId();
        final Worker worker = workerDao.findById(workerId)
                .orElseThrow(() -> new ResourceNotFoundException(Worker.class, workerId));

        return ShiftAssignment.builder()
                .worker(worker)
                .shift(dto.getShift())
                .shiftDate(dto.getShiftDate())
                .build();
    }

    public ShiftAssignmentDto toDto(ShiftAssignment entity) {
        return ShiftAssignmentDto.builder()
                .id(entity.getId())
                .workerId(entity.getWorker().getId())
                .shift(entity.getShift())
                .shiftDate(entity.getShiftDate())
                .build();
    }
}
