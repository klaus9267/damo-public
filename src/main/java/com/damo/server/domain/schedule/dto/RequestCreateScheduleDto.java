package com.damo.server.domain.schedule.dto;

import com.damo.server.domain.schedule.entity.ScheduleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * {@code RequestCreateScheduleDto}는 일정 생성 요청을 나타내는 DTO 클래스입니다.
 */
public record RequestCreateScheduleDto(
    @NotNull(message = "eventDate is required")
    @Schema(description = "거래 날짜", example = "2024-01-02T00:00:00")
    LocalDateTime eventDate,

    @NotBlank(message = "event is required")
    @Schema(description = "행사 이름", example = "민호 취업 축하 파티")
    String event,

    @Length(max = 200, message = "memo max length 200")
    @Schema(description = "없거나 최대 200자", example = "메모가 사는 곳은 메모도 메모시 memory 엌ㅋㅋㅋㅋ")
    String memo,

    @Schema(description = "일정 중요도", example = "NORMAL")
    ScheduleStatus status,

    @Schema(description = "거래 내역 id", example = "1", nullable = true)
    Long transactionId
) {
}
