package io.pidabrow.shiftmanager.mapper;

import io.pidabrow.shiftmanager.domain.Worker;
import io.pidabrow.shiftmanager.dto.response.FullWorkerDto;
import io.pidabrow.shiftmanager.dto.response.ShiftAssignmentDto;
import io.pidabrow.shiftmanager.dto.request.WorkerCreateDto;
import io.pidabrow.shiftmanager.dto.request.WorkerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WorkerMapper {

    private final ShiftAssignmentMapper shiftAssignmentMapper;

    public Worker toEntity(WorkerCreateDto workerCreateDto) {
        return Worker.builder()
                .fullName(workerCreateDto.getFullName())
                .phoneNumber(workerCreateDto.getPhoneNumber())
                .build();
    }

    public WorkerDto toDto(Worker worker) {
        return WorkerDto.builder()
                .id(worker.getId())
                .fullName(worker.getFullName())
                .phoneNumber(worker.getPhoneNumber())
                .build();
    }

    public FullWorkerDto toFullDto(Worker worker) {
        List<ShiftAssignmentDto> shiftAssignmentDtos = worker.getShiftAssignments().stream()
                .map(shiftAssignmentMapper::toDto)
                .collect(Collectors.toList());

        return FullWorkerDto.builder()
                .id(worker.getId())
                .fullName(worker.getFullName())
                .phoneNumber(worker.getPhoneNumber())
                .shifts(shiftAssignmentDtos)
                .build();
    }

    public Worker merge(Worker worker, WorkerDto workerDto) {
        worker.setFullName(workerDto.getFullName());
        worker.setPhoneNumber(workerDto.getPhoneNumber());

        return worker;
    }
}
