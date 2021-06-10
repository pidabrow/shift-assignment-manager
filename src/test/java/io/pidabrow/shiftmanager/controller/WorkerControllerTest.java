package io.pidabrow.shiftmanager.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.pidabrow.shiftmanager.dao.WorkerDao;
import io.pidabrow.shiftmanager.domain.Worker;
import io.pidabrow.shiftmanager.dto.WorkerCreateDto;
import io.pidabrow.shiftmanager.dto.WorkerDto;
import io.pidabrow.shiftmanager.mapper.WorkerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class WorkerControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private WorkerDao workerDao;
    @Autowired private WorkerMapper workerMapper;

    @BeforeEach
    public void beforeEach() {
        workerDao.deleteAll();
    }

    @Test
    public void shouldGetWorkerById() throws Exception {
        var worker = workerDao.save(Worker.builder().fullName("John Smith").build());

        mockMvc.perform(get("/api/workers/{id}", worker.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(worker.getId().intValue())))
                .andExpect(jsonPath("$.fullName", is("John Smith")));
    }

    @Test
    public void shouldNotGetWorkerById_workerWithGivenIdDoesntExist() throws Exception {
        mockMvc.perform(get("/api/workers/{id}", 1))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetAllWorkers() throws Exception {
        var worker1 = Worker.builder().fullName("John Smith").build();
        var worker2 = Worker.builder().fullName("John Snow").build();
        var worker3 = Worker.builder().fullName("Paul Jones").build();
        workerDao.saveAll(List.of(worker1, worker2, worker3));

        String response = mockMvc.perform(get("/api/workers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<WorkerDto> workerDtos = objectMapper.readValue(response, new TypeReference<>() {
        });

        assertThat(workerDtos).hasSize(3);
        assertThat(workerDtos).containsExactlyInAnyOrder(
                workerMapper.toDto(worker1),
                workerMapper.toDto(worker2),
                workerMapper.toDto(worker3)
        );
    }

    @Test
    public void shouldGetAllWorkers_returnEmptyList() throws Exception {
        String response = mockMvc.perform(get("/api/workers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<WorkerDto> workerDtos = objectMapper.readValue(response, new TypeReference<>() {});

        assertThat(workerDtos).hasSize(0);
    }

    @Test
    public void shouldDeleteWorker() throws Exception {
        var worker = workerDao.save(Worker.builder().fullName("John Smith").build());

        mockMvc.perform(delete("/api/workers/{id}", worker.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldNotDeleteWorker_workerWithGivenIdDoesntExist() throws Exception {
        mockMvc.perform(delete("/api/workers/{id}", 1))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldSaveWorker() throws Exception {
        var workerCreateDto = WorkerCreateDto.builder().fullName("John Smith").build();

        mockMvc.perform(post("/api/workers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(workerCreateDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.fullName", is("John Smith")));
    }

    @Test
    public void shouldNotSaveWorker_missingFullName() throws Exception {
        var workerCreateDto = WorkerCreateDto.builder().build();

        mockMvc.perform(post("/api/workers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(workerCreateDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fullName", is("must not be blank")));
    }

    @Test
    public void shouldNotFoundWorker() throws Exception {
        mockMvc.perform(get("/workers/{id}", 1))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateWorker() throws Exception {
        var worker = workerDao.save(Worker.builder().fullName("John Smith").build());
        var workerDto = WorkerDto.builder().id(worker.getId()).fullName("John Snow").build();

        mockMvc.perform(put("/api/workers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(workerDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.fullName", is("John Snow")));
    }

    @Test
    public void shouldNotUpdateWorker_missingWorkerId() throws Exception {
        var worker = workerDao.save(Worker.builder().fullName("John Smith").build());
        var workerDto = WorkerDto.builder().fullName("John Snow").build();

        mockMvc.perform(put("/api/workers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(workerDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id", is("must not be null")));
    }

    @Test
    public void shouldNotUpdateWorker_missingUpdatedFullName() throws Exception {
        var worker = workerDao.save(Worker.builder().fullName("John Smith").build());
        var workerDto = WorkerDto.builder().id(worker.getId()).build();

        mockMvc.perform(put("/api/workers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(workerDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fullName", is("must not be blank")));
    }

    @Test
    public void shouldNotUpdateWorker_emptyUpdatedFullName() throws Exception {
        var worker = workerDao.save(Worker.builder().fullName("John Smith").build());
        var workerDto = WorkerDto.builder().id(worker.getId()).fullName("").build();

        mockMvc.perform(put("/api/workers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(workerDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fullName", is("must not be blank")));
    }

    @Test
    public void shouldNotUpdateWorker_WorkerWithGivenIdDoesntExist() throws Exception {
        var worker = workerDao.save(Worker.builder().fullName("John Smith").build());
        var nonExistingId = worker.getId() + 1;
        var workerDto = WorkerDto.builder().id(worker.getId() + 1).fullName("John Snow").build();

        mockMvc.perform(put("/api/workers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(workerDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Cannot find io.pidabrow.shiftmanager.domain.Worker with id= " + nonExistingId)));
    }
}
