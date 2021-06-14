package io.pidabrow.shiftmanager.dto.response;

import io.pidabrow.shiftmanager.domain.Shift;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
@Builder
public class ShiftAssignmentDto {
    @NotNull Long id;

    @NotNull
    private Long workerId;

    @NotBlank
    private Shift shift;

    @NotNull
    private LocalDate shiftDate;
}
