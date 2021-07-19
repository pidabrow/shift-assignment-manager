package io.pidabrow.shiftmanager.dto.response;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@Builder
public class FullWorkerDto {
    @NotNull
    private final Long id;

    @NotBlank
    private final String fullName;

    @NotBlank
    private final String phoneNumber;

    private final List<ShiftAssignmentDto> shifts;
}
