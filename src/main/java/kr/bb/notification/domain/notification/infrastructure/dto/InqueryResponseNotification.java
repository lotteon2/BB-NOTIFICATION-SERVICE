package kr.bb.notification.domain.notification.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class InqueryResponseNotification {
    private Long userId;
    private String phoneNumber;
}
