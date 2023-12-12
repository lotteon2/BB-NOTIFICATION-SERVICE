package kr.bb.notification.domain.notification.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestionRegister {
    private Long storeId;
}
