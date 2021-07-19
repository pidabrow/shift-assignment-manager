package io.pidabrow.shiftmanager.dto;

import io.pidabrow.shiftmanager.domain.ShiftAssignment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SmsDto {
    private final String phoneNumber;
    private final String messageBody;

    public static SmsDto of(ShiftAssignment shiftAssignment) {
        var worker = shiftAssignment.getWorker();

        var phoneNumber = worker.getPhoneNumber();
        var messageBody = String.format("Hello %1$s! A new shift was assigned to you. Shift date: %2$s, shift type: %3$s",
                worker.getFullName(),
                shiftAssignment.getShiftDate().toString(),
                shiftAssignment.getShift());

        return new SmsDto(phoneNumber, messageBody);
    }
}
