package io.pidabrow.shiftmanager.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerCreateDto {
    @NotBlank
    private String fullName;
}
