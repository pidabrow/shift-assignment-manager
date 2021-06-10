package io.pidabrow.shiftmanager.service;

import io.pidabrow.shiftmanager.dao.WorkerDao;
import io.pidabrow.shiftmanager.domain.Worker;
import io.pidabrow.shiftmanager.dto.FullWorkerDto;
import io.pidabrow.shiftmanager.dto.WorkerCreateDto;
import io.pidabrow.shiftmanager.dto.WorkerDto;
import io.pidabrow.shiftmanager.exception.ResourceNotFoundException;
import io.pidabrow.shiftmanager.mapper.WorkerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkerService {
    private final WorkerDao workerDao;
    private final WorkerMapper workerMapper;

    @Transactional
    public WorkerDto createWorker(WorkerCreateDto workerCreateDto) {
        Worker worker = workerMapper.toEntity(workerCreateDto);

        return workerMapper.toDto(workerDao.save(worker));
    }

    @Transactional(readOnly = true)
    public FullWorkerDto findWorkerByIdEagerly(Long id) {
        return workerMapper.toFullDto(workerDao.findWorkerByIdEagerly(id)
            .orElseThrow(() -> new ResourceNotFoundException(Worker.class, id))
        );
    }

    @Transactional(readOnly = true)
    public List<WorkerDto> findAllWorkers() {
        return workerDao.findAll()
                .stream()
                .map(workerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkerDto updateWorker(WorkerDto workerDto) {
        final Worker toUpdate = findByIdInternal(workerDto.getId());
        final Worker updated = workerMapper.merge(toUpdate, workerDto);

        return workerMapper.toDto(updated);
    }

    @Transactional
    public void deleteWorkerById(Long id) {
        Worker toDelete = findByIdInternal(id);
        workerDao.delete(toDelete);
    }

    private Worker findByIdInternal(Long id) {
        return workerDao.findById(id).orElseThrow(() -> new ResourceNotFoundException(Worker.class, id));
    }
}
