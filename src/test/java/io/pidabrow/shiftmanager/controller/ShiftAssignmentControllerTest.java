package io.pidabrow.shiftmanager.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.pidabrow.shiftmanager.dao.ShiftAssignmentDao;
import io.pidabrow.shiftmanager.dao.WorkerDao;
import io.pidabrow.shiftmanager.domain.Shift;
import io.pidabrow.shiftmanager.domain.ShiftAssignment;
import io.pidabrow.shiftmanager.domain.Worker;
import io.pidabrow.shiftmanager.dto.RemoveShiftAssignmentDto;
import io.pidabrow.shiftmanager.dto.ShiftAssignmentCreateDto;
import io.pidabrow.shiftmanager.dto.ShiftAssignmentDto;
import io.pidabrow.shiftmanager.mapper.ShiftAssignmentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ShiftAssignmentControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ShiftAssignmentDao shiftAssignmentDao;
    @Autowired private WorkerDao workerDao;
    @Autowired private ShiftAssignmentMapper shiftAssignmentMapper;

    private Worker worker;

    @BeforeEach
    public void beforeEach() {
        workerDao.deleteAll();
        worker = workerDao.save(Worker.builder().fullName("John Smith").build());
    }

    @Test
    public void shouldGetShiftAssignmentsForWorker() throws Exception {
        var shift1 = shiftAssignmentDao.save(ShiftAssignment.builder()
                .worker(worker)
                .shiftDate(LocalDate.now())
                .shift(Shift.SHIFT_8_16)
                .build());

        var shift2 = shiftAssignmentDao.save(ShiftAssignment.builder()
                .worker(worker)
                .shiftDate(LocalDate.now().minusDays(1))
                .shift(Shift.SHIFT_8_16)
                .build());

        String response = mockMvc.perform(get("/api/shifts?workerId={id}", worker.getId()))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<ShiftAssignmentDto> shiftAssignmentDtos = objectMapper.readValue(response, new TypeReference<>() {
        });

        assertThat(shiftAssignmentDtos).hasSize(2);
        assertThat(shiftAssignmentDtos).containsExactlyInAnyOrder(
                shiftAssignmentMapper.toDto(shift1),
                shiftAssignmentMapper.toDto(shift2)
        );
    }

    @Test
    public void shouldGetShiftAssignmentsForWorker_returnEmptyList() throws Exception {
        String response = mockMvc.perform(get("/api/shifts?workerId={id}", worker.getId()))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<ShiftAssignmentDto> shiftAssignmentDtos = objectMapper.readValue(response, new TypeReference<>() {
        });

        assertThat(shiftAssignmentDtos).isEmpty();
    }

    @Test
    public void shouldGetShiftAssignmentsForWorker_returnEmptyList2() throws Exception {
        var differentWorker = workerDao.save(Worker.builder().fullName("John Snow").build());
        shiftAssignmentDao.save(ShiftAssignment.builder()
                .worker(differentWorker)
                .shiftDate(LocalDate.now())
                .shift(Shift.SHIFT_8_16)
                .build());

        String response = mockMvc.perform(get("/api/shifts?workerId={id}", worker.getId()))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<ShiftAssignmentDto> shiftAssignmentDtos = objectMapper.readValue(response, new TypeReference<>() {
        });

        assertThat(shiftAssignmentDtos).isEmpty();
    }

    @Test
    public void shouldSaveShiftAssignmentForWorker() throws Exception {
        var shiftAssignmentCreateDto = ShiftAssignmentCreateDto.builder()
                .workerId(worker.getId())
                .shiftDate(LocalDate.now())
                .shift(Shift.SHIFT_8_16)
                .build();

        mockMvc.perform(post("/api/shifts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(shiftAssignmentCreateDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.workerId", is(worker.getId().intValue())))
                .andExpect(jsonPath("$.shift", is("SHIFT_8_16")))
                .andExpect(jsonPath("$.shiftDate", is(LocalDate.now().toString())));
    }

    @Test
    public void shouldNotSaveShiftAssignmentForWorker_workerDoesntExist() throws Exception {
        var notExistingWorkerId = worker.getId() + 1;
        var shiftAssignmentCreateDto = ShiftAssignmentCreateDto.builder()
                .workerId(notExistingWorkerId)
                .shiftDate(LocalDate.now())
                .shift(Shift.SHIFT_8_16)
                .build();

        mockMvc.perform(post("/api/shifts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(shiftAssignmentCreateDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldNotSaveShiftAssignmentForWorker_missingParameters() throws Exception {
        var shiftAssignmentCreateDto = ShiftAssignmentCreateDto.builder()
                .build();

        mockMvc.perform(post("/api/shifts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(shiftAssignmentCreateDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.workerId", is("must not be null")))
                .andExpect(jsonPath("$.shift", is("must not be null")))
                .andExpect(jsonPath("$.shiftDate", is("must not be null")));
    }

    @Test
    public void shouldNotSaveShiftAssignmentForWorker_alreadyExistShiftAssignmentForShiftDate() throws Exception {
        var workerId = worker.getId();
        var today = LocalDate.now();
        shiftAssignmentDao.save(ShiftAssignment.builder()
                .worker(worker)
                .shiftDate(today)
                .shift(Shift.SHIFT_8_16)
                .build());

        var shiftAssignmentCreateDto = ShiftAssignmentCreateDto.builder()
                .workerId(workerId)
                .shiftDate(today)
                .shift(Shift.SHIFT_16_24)
                .build();

        mockMvc.perform(post("/api/shifts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(shiftAssignmentCreateDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("There's already shift assignment for Worker " + workerId + " for " + today.toString())));
    }

    @Test
    public void shouldDeleteShiftAssignment() throws Exception {
        var today = LocalDate.now();
        shiftAssignmentDao.save(ShiftAssignment.builder()
                .worker(worker)
                .shiftDate(today)
                .shift(Shift.SHIFT_8_16)
                .build());

        var removeShiftAssignmentDto = RemoveShiftAssignmentDto.builder()
                .shiftDate(today)
                .workerId(worker.getId())
                .build();

        mockMvc.perform(delete("/api/shifts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(removeShiftAssignmentDto)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldNotDeleteShiftAssignment_missingParameters() throws Exception {
        var removeShiftAssignmentDto = RemoveShiftAssignmentDto.builder().build();

        mockMvc.perform(delete("/api/shifts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(removeShiftAssignmentDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.workerId", is("must not be null")))
                .andExpect(jsonPath("$.shiftDate", is("must not be null")));
    }

    @Test
    public void shouldNotDeleteShiftAssignment_shiftAssignmentDoesntExist() throws Exception {
        var today = LocalDate.now();
        var removeShiftAssignmentDto = RemoveShiftAssignmentDto.builder()
                .workerId(worker.getId())
                .shiftDate(today)
                .build();

        mockMvc.perform(delete("/api/shifts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(removeShiftAssignmentDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}