package io.pidabrow.shiftmanager.service;

import io.pidabrow.shiftmanager.dao.ShiftAssignmentDao;
import io.pidabrow.shiftmanager.dao.WorkerDao;
import io.pidabrow.shiftmanager.domain.ShiftAssignment;
import io.pidabrow.shiftmanager.domain.Worker;
import io.pidabrow.shiftmanager.dto.RemoveShiftAssignmentDto;
import io.pidabrow.shiftmanager.dto.ShiftAssignmentCreateDto;
import io.pidabrow.shiftmanager.dto.ShiftAssignmentDto;
import io.pidabrow.shiftmanager.exception.DomainException;
import io.pidabrow.shiftmanager.exception.ResourceNotFoundException;
import io.pidabrow.shiftmanager.mapper.ShiftAssignmentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShiftAssignmentService {

    private final ShiftAssignmentDao shiftAssignmentDao;
    private final WorkerDao workerDao;
    private final ShiftAssignmentMapper shiftAssignmentMapper;

    public ShiftAssignmentService(ShiftAssignmentDao shiftAssignmentDao,
                                  WorkerDao workerDao,
                                  ShiftAssignmentMapper shiftAssignmentMapper) {
        this.shiftAssignmentDao = shiftAssignmentDao;
        this.workerDao = workerDao;
        this.shiftAssignmentMapper = shiftAssignmentMapper;
    }

    @Transactional
    public ShiftAssignmentDto createShiftAssignment(ShiftAssignmentCreateDto dto) {
        var workerId = dto.getWorkerId();
        var shiftDate = dto.getShiftDate();

        if(!workerDao.existsById(workerId)) {
            throw new ResourceNotFoundException(Worker.class, workerId);
        }

        if(!shiftAssignmentDao.existsShiftAssignmentByWorkerIdAndShiftDate(workerId, shiftDate)) {
            ShiftAssignment shiftAssignment = shiftAssignmentMapper.toEntity(dto);
            return shiftAssignmentMapper.toDto(shiftAssignmentDao.save(shiftAssignment));
        } else {
            throw new DomainException("There's already shift assignment for Worker " + workerId + " for " + dto.getShiftDate());
        }
    }

    @Transactional
    public void removeShiftAssignment(RemoveShiftAssignmentDto dto) {
        var workerId = dto.getWorkerId();
        var shiftDate = dto.getShiftDate();

        Optional<ShiftAssignment> shiftAssignment = shiftAssignmentDao.findShiftAssignmentByWorkerIdAndShiftDate(workerId, shiftDate);
        if(shiftAssignment.isEmpty()) {
            throw new ResourceNotFoundException(ShiftAssignment.class);
        } else {
            shiftAssignment.ifPresent(shiftAssignmentDao::delete);
        }
    }

    @Transactional(readOnly = true)
    public List<ShiftAssignmentDto> getShiftAssignmentsForWorker(Long workerId) {
        return shiftAssignmentDao.findAllByWorkerId(workerId)
                .stream()
                .map(shiftAssignmentMapper::toDto)
                .collect(Collectors.toList());
    }
}
