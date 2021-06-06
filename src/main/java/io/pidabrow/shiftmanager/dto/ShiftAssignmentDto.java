package io.pidabrow.shiftmanager.dto;

import io.pidabrow.shiftmanager.domain.Shift;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
@Builder
public class ShiftAssignmentDto {
    @NotNull
    private Long id;

    @NotNull
    private Long workerId;

    @NotBlank
    private Shift shift;

    @NotBlank
    private LocalDate shiftDate;
}
