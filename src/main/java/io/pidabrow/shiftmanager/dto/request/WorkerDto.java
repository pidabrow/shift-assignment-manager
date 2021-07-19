package io.pidabrow.shiftmanager.dto.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder
public class WorkerDto {
    @NotNull
    private Long id;

    @NotBlank
    private String fullName;

    @NotBlank
    private String phoneNumber;
}
