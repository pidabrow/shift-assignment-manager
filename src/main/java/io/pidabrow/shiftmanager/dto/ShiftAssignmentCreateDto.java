package io.pidabrow.shiftmanager.dto;

import io.pidabrow.shiftmanager.domain.Shift;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftAssignmentCreateDto {
    @NotNull
    private Long workerId;

    @NotNull
    private Shift shift;

    @NotNull
    private LocalDate shiftDate;
}
