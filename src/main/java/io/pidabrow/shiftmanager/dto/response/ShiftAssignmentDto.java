package io.pidabrow.shiftmanager.dto.response;

import io.pidabrow.shiftmanager.domain.Shift;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class ShiftAssignmentDto {
    @NotNull
    private final Long id;

    @NotNull
    private final Long workerId;

    @NotBlank
    private final Shift shift;

    @NotNull
    private final LocalDate shiftDate;
}
