package io.pidabrow.shiftmanager.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RemoveShiftAssignmentDto {
    @NotNull
    private Long workerId;

    @NotNull
    private LocalDate shiftDate;
}
