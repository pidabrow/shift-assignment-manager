package io.pidabrow.shiftmanager.dto.response;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Value
@Builder
public class FullWorkerDto {
    @NotNull
    private Long id;

    @NotBlank
    private String fullName;

    private List<ShiftAssignmentDto> shifts;
}
