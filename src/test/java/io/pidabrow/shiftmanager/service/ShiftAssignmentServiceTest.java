package io.pidabrow.shiftmanager.service;

import io.pidabrow.shiftmanager.dao.ShiftAssignmentDao;
import io.pidabrow.shiftmanager.dao.WorkerDao;
import io.pidabrow.shiftmanager.domain.Shift;
import io.pidabrow.shiftmanager.domain.ShiftAssignment;
import io.pidabrow.shiftmanager.domain.Worker;
import io.pidabrow.shiftmanager.dto.ShiftAssignmentCreateDto;
import io.pidabrow.shiftmanager.dto.ShiftAssignmentDto;
import io.pidabrow.shiftmanager.exception.DomainException;
import io.pidabrow.shiftmanager.mapper.ShiftAssignmentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShiftAssignmentServiceTest {

    @Mock private ShiftAssignmentDao shiftAssignmentDao;
    @Mock private WorkerDao workerDao;
    private ShiftAssignmentMapper shiftAssignmentMapper;
    private ShiftAssignmentService shiftAssignmentService;

    @BeforeEach
    void initShiftAssignmentService() {
        shiftAssignmentMapper = Mockito.spy(new ShiftAssignmentMapper(workerDao));
        shiftAssignmentService = new ShiftAssignmentService(shiftAssignmentDao, workerDao, shiftAssignmentMapper);
    }

    @Test
    public void shouldGetShiftAssignmentsForWorker() {
        // given
        final Long workerId = 1L;
        Worker worker1 = Worker.builder()
                .id(workerId)
                .fullName("John Smith")
                .build();

        ShiftAssignment shift1 = ShiftAssignment.builder()
                .id(1L)
                .shift(Shift.SHIFT_0_8)
                .shiftDate(LocalDate.now())
                .worker(worker1)
                .build();

        ShiftAssignment shift2 = ShiftAssignment.builder()
                .id(2L)
                .shift(Shift.SHIFT_0_8)
                .shiftDate(LocalDate.now().minusDays(1))
                .worker(worker1)
                .build();

        List<ShiftAssignment> shiftAssignments = new ArrayList<>();
        shiftAssignments.add(shift1);
        shiftAssignments.add(shift2);

        // when
        when(shiftAssignmentDao.findAllByWorkerId(workerId)).thenReturn(shiftAssignments);

        // then
        List<ShiftAssignmentDto> shiftAssignmentDtos = shiftAssignmentService.getShiftAssignmentsForWorker(workerId);

        assertThat(shiftAssignmentDtos.size()).isEqualTo(2);
        assertThat(shiftAssignmentDtos).containsExactlyInAnyOrder(
                shiftAssignmentMapper.toDto(shift1),
                shiftAssignmentMapper.toDto(shift2)
        );
    }

    @Test
    public void shouldAssignShiftAssignmentToWorker() {
        // given
        final var workerId = 1L;
        Worker worker1 = Worker.builder()
                .id(workerId)
                .fullName("John Smith")
                .build();

        ShiftAssignment shift1 = ShiftAssignment.builder()
                .id(1L)
                .shift(Shift.SHIFT_0_8)
                .shiftDate(LocalDate.now())
                .worker(worker1)
                .build();

        List<ShiftAssignment> shiftAssignments = new ArrayList<>();
        shiftAssignments.add(shift1);

        // when
        when(shiftAssignmentDao.findAllByWorkerId(workerId)).thenReturn(shiftAssignments);

        // then
        List<ShiftAssignmentDto> shiftAssignmentDtos = shiftAssignmentService.getShiftAssignmentsForWorker(workerId);

        assertThat(shiftAssignmentDtos.size()).isEqualTo(1);
        assertThat(shiftAssignmentDtos).containsExactlyInAnyOrder(
                shiftAssignmentMapper.toDto(shift1)
        );
    }

    @Test
    public void shouldNotAssignShiftAssignmentToWorker() {
        // given
        final var workerId = 1L;
        final var today = LocalDate.now();
        final var expectedMessage = "There's already shift assignment for Worker 1 for " + today.toString();

        ShiftAssignmentCreateDto shift = ShiftAssignmentCreateDto.builder()
                .shift(Shift.SHIFT_8_16)
                .shiftDate(today)
                .workerId(workerId)
                .build();

        // when
        when(workerDao.existsById(workerId)).thenReturn(true);
        when(shiftAssignmentDao.existsShiftAssignmentByWorkerIdAndShiftDate(workerId, today)).thenReturn(true);

        // then
        DomainException exception = assertThrows(DomainException.class, () -> shiftAssignmentService.createShiftAssignment(shift));
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }
}
