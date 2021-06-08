package io.pidabrow.shiftmanager.controller;

import io.pidabrow.shiftmanager.dto.FullWorkerDto;
import io.pidabrow.shiftmanager.dto.WorkerCreateDto;
import io.pidabrow.shiftmanager.dto.WorkerDto;
import io.pidabrow.shiftmanager.service.ValidationService;
import io.pidabrow.shiftmanager.service.WorkerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
public class WorkerController {

    private final ValidationService validationService;
    private final WorkerService workerService;

    @PostMapping("/api/workers")
    @ApiOperation(value = "Creates Worker based on given payload.", response = WorkerDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "A new Worker was created successfully."),
            @ApiResponse(code = 400, message = "Cannot create new Worker due to mandatory parameter missing or wrong format.")
    })
    public ResponseEntity<?> handlePostCreateWorker(@Valid @RequestBody WorkerCreateDto workerCreateDto,
                                                    BindingResult bindingResult) {
        ResponseEntity<?> validationError = validationService.validateBindingResult(bindingResult);
        if (validationError != null) {
            return validationError;
        }

        return new ResponseEntity(workerService.createWorker(workerCreateDto), CREATED);
    }

    @GetMapping("/api/workers/{id}")
    @ApiOperation(value = "Retrieves Worker by id.", response = FullWorkerDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Worker retrieved successfully (by worker id)."),
            @ApiResponse(code = 404, message = "A Worker with the specified id was not found.")
    })
    public ResponseEntity<?> handleGetWorkerById(@PathVariable Long id) {
        return ResponseEntity.ok(workerService.findWorkerByIdEagerly(id));
    }

    @GetMapping("/api/workers")
    @ApiOperation(value = "Retrieves all Workers.", response = WorkerDto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all Workers retrieved successfully.")
    })
    public ResponseEntity<?> handleGetAllWorkers() {
        return ResponseEntity.ok(workerService.findAllWorkers());
    }

    @PutMapping("/api/workers")
    @ApiOperation(value = "Updates Worker based on given payload.", response = WorkerDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Worker updated successfully."),
            @ApiResponse(code = 400, message = "Cannot update an existing Worker due to mandatory parameter missing or wrong format."),
            @ApiResponse(code = 404, message = "A Worker with the specified id was not found.")
    })
    public ResponseEntity<?> handlePutUpdateWorker(@Valid @RequestBody WorkerDto workerDto,
                                                   BindingResult bindingResult) {
        ResponseEntity<?> validationError = validationService.validateBindingResult(bindingResult);
        if (validationError != null) {
            return validationError;
        }

        return ResponseEntity.ok(workerService.updateWorker(workerDto));
    }

    @DeleteMapping("/api/workers/{id}")
    @ApiOperation(value = "Deletes Worker by id.", response = WorkerDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Worker was deleted successfully.")
    })
    public ResponseEntity<?> handleDeleteWorker(@PathVariable Long id) {
        workerService.deleteWorkerById(id);

        return ResponseEntity.noContent().build();
    }
}
