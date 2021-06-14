package io.pidabrow.shiftmanager.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShiftAssignmentRemoveDto {
    @NotNull
    private Long workerId;

    @NotNull
    private LocalDate shiftDate;
}
