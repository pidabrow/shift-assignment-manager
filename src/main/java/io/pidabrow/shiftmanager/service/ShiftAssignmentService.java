package io.pidabrow.shiftmanager.service;

import io.pidabrow.shiftmanager.dao.ShiftAssignmentDao;
import io.pidabrow.shiftmanager.dao.WorkerDao;
import io.pidabrow.shiftmanager.domain.ShiftAssignment;
import io.pidabrow.shiftmanager.domain.Worker;
import io.pidabrow.shiftmanager.dto.SmsDto;
import io.pidabrow.shiftmanager.dto.request.ShiftAssignmentRemoveDto;
import io.pidabrow.shiftmanager.dto.request.ShiftAssignmentCreateDto;
import io.pidabrow.shiftmanager.dto.response.ShiftAssignmentDto;
import io.pidabrow.shiftmanager.exception.DomainException;
import io.pidabrow.shiftmanager.exception.ResourceNotFoundException;
import io.pidabrow.shiftmanager.mapper.ShiftAssignmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftAssignmentService {

    private final ShiftAssignmentDao shiftAssignmentDao;
    private final WorkerDao workerDao;
    private final ShiftAssignmentMapper shiftAssignmentMapper;
    private final SmsService smsService;

    @Transactional
    public ShiftAssignmentDto createShiftAssignment(ShiftAssignmentCreateDto dto) {
        var workerId = dto.getWorkerId();
        var shiftDate = dto.getShiftDate();

        if(!workerDao.existsById(workerId)) {
            throw new ResourceNotFoundException(Worker.class, workerId);
        }

        if(shiftAssignmentDao.existsShiftAssignmentByWorkerIdAndShiftDate(workerId, shiftDate)) {
            throw new DomainException("There's already shift assignment for Worker " + workerId + " for " + dto.getShiftDate());
        }

        ShiftAssignment shiftAssignment = shiftAssignmentMapper.toEntity(dto);
        ShiftAssignmentDto shiftAssignmentDto = shiftAssignmentMapper.toDto(shiftAssignmentDao.save(shiftAssignment));

        smsService.sendSmsNotification(SmsDto.of(shiftAssignment));

        return shiftAssignmentDto;
    }

    @Transactional
    public void removeShiftAssignment(ShiftAssignmentRemoveDto dto) {
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
