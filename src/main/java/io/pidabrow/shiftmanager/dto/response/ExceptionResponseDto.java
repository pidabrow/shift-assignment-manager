package io.pidabrow.shiftmanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExceptionResponseDto {
    private final String code;
    private final String message;
}