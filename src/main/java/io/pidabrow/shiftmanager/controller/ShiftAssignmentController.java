package io.pidabrow.shiftmanager.controller;

import io.pidabrow.shiftmanager.dto.RemoveShiftAssignmentDto;
import io.pidabrow.shiftmanager.dto.ShiftAssignmentCreateDto;
import io.pidabrow.shiftmanager.dto.ShiftAssignmentDto;
import io.pidabrow.shiftmanager.dto.WorkerDto;
import io.pidabrow.shiftmanager.service.ShiftAssignmentService;
import io.pidabrow.shiftmanager.service.ValidationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
public class ShiftAssignmentController {

    private final ShiftAssignmentService shiftAssignmentService;
    private final ValidationService validationService;

    @PostMapping("/api/shifts")
    @ApiOperation(value = "Creates new Shift Assignment for given Worker and shift date.", response = ShiftAssignmentDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Shift Assignment was created successfully."),
            @ApiResponse(code = 400, message = "Cannot create Shift Assignment due to mandatory parameter missing," +
                    " wrong format or already existing Shift Assignment with the same shift date."),
            @ApiResponse(code = 404, message = "A Worker with the specified id was not found")
    })
    public ResponseEntity<?> handleCreateShiftAssignment(@Valid @RequestBody ShiftAssignmentCreateDto dto, BindingResult bindingResult) {
        ResponseEntity<?> validationError = validationService.validateBindingResult(bindingResult);
        if (validationError != null) {
            return validationError;
        }

        return ResponseEntity.ok(shiftAssignmentService.createShiftAssignment(dto));
    }

    @DeleteMapping("/api/shifts")
    @ApiOperation(value = "Retrieves all Shift Assignments for given worker id.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Shift Assignment was deleted successfully."),
            @ApiResponse(code = 400, message = "Cannot delete Shift Assignment due to mandatory parameter missing or wrong format."),
            @ApiResponse(code = 404, message = "A Shift Assignment with the specified worker id and shift date was not found.")
    })
    public ResponseEntity<?> removeShiftAssignment(@Valid @RequestBody RemoveShiftAssignmentDto dto, BindingResult bindingResult) {
        ResponseEntity<?> validationError = validationService.validateBindingResult(bindingResult);
        if (validationError != null) {
            return validationError;
        }

        shiftAssignmentService.removeShiftAssignment(dto);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/shifts")
    @ApiOperation(value = "Retrieves all Shift Assignments for given worker id.", response = ShiftAssignmentDto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all Shift Assignments for given Worker retrieved successfully."),
            @ApiResponse(code = 404, message = "A Worker with the specified id was not found.")
    })
    public ResponseEntity<?> handleGetShiftAssignmentsForWorker(@RequestParam Long workerId) {
        return ResponseEntity.ok(shiftAssignmentService.getShiftAssignmentsForWorker(workerId));
    }
}
