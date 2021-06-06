package io.pidabrow.shiftmanager.controller;

import io.pidabrow.shiftmanager.dto.RemoveShiftAssignmentDto;
import io.pidabrow.shiftmanager.dto.ShiftAssignmentCreateDto;
import io.pidabrow.shiftmanager.service.ShiftAssignmentService;
import io.pidabrow.shiftmanager.service.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ShiftAssignmentController {

    private final ShiftAssignmentService shiftAssignmentService;
    private final ValidationService validationService;

    public ShiftAssignmentController(ShiftAssignmentService shiftAssignmentService, ValidationService validationService) {
        this.shiftAssignmentService = shiftAssignmentService;
        this.validationService = validationService;
    }

    @PostMapping("/api/shifts")
    public ResponseEntity<?> handleCreateShiftAssignment(@Valid @RequestBody ShiftAssignmentCreateDto dto, BindingResult bindingResult) {
        ResponseEntity<?> validationError = validationService.validateBindingResult(bindingResult);
        if (validationError != null) {
            return validationError;
        }

        return ResponseEntity.ok(shiftAssignmentService.createShiftAssignment(dto));
    }

    @DeleteMapping("/api/shifts")
    public ResponseEntity<?> removeShiftAssignment(@Valid @RequestBody RemoveShiftAssignmentDto dto, BindingResult bindingResult) {
        ResponseEntity<?> validationError = validationService.validateBindingResult(bindingResult);
        if (validationError != null) {
            return validationError;
        }

        shiftAssignmentService.removeShiftAssignment(dto);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/shifts")
    public ResponseEntity<?> handleGetShiftAssignmentsForWorker(@RequestParam Long workerId) {
        return ResponseEntity.ok(shiftAssignmentService.getShiftAssignmentsForWorker(workerId));
    }
}
