package io.pidabrow.shiftmanager.controller;

import io.pidabrow.shiftmanager.dto.WorkerCreateDto;
import io.pidabrow.shiftmanager.dto.WorkerDto;
import io.pidabrow.shiftmanager.service.ValidationService;
import io.pidabrow.shiftmanager.service.WorkerService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class WorkerController {

    private final ValidationService validationService;
    private final WorkerService workerService;

    public WorkerController(ValidationService validationService, WorkerService workerService) {
        this.validationService = validationService;
        this.workerService = workerService;
    }

    @PostMapping("/api/workers")
    public ResponseEntity<?> handlePostCreateWorker(@Valid @RequestBody WorkerCreateDto workerCreateDto,
                                                    BindingResult bindingResult) {
        ResponseEntity<?> validationError = validationService.validateBindingResult(bindingResult);
        if (validationError != null) {
            return validationError;
        }

        return new ResponseEntity(workerService.createWorker(workerCreateDto), CREATED);
    }

    @GetMapping("/api/workers/{id}")
    public ResponseEntity<?> handleGetWorkerById(@PathVariable Long id) {
        return ResponseEntity.ok(workerService.findWorkerByIdEagerly(id));
    }

    @GetMapping("/api/workers")
    public ResponseEntity<?> handleGetAllWorkers() {
        return ResponseEntity.ok(workerService.findAllWorkers());
    }

    @PutMapping("/api/workers")
    public ResponseEntity<?> handlePutUpdateWorker(@Valid @RequestBody WorkerDto workerDto,
                                                   BindingResult bindingResult) {
        ResponseEntity<?> validationError = validationService.validateBindingResult(bindingResult);
        if (validationError != null) {
            return validationError;
        }

        return ResponseEntity.ok(workerService.updateWorker(workerDto));
    }

    @DeleteMapping("/api/workers/{id}")
    public ResponseEntity<?> handleDeleteWorker(@PathVariable Long id) {
        workerService.deleteWorkerById(id);

        return ResponseEntity.noContent().build();
    }
}
