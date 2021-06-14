package io.pidabrow.shiftmanager.dto.request;

import io.pidabrow.shiftmanager.domain.Shift;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShiftAssignmentCreateDto {
    @NotNull
    private Long workerId;

    @NotNull
    private Shift shift;

    @NotNull
    private LocalDate shiftDate;
}
