package io.pidabrow.shiftmanager.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkerCreateDto {
    @NotBlank
    private String fullName;
}
