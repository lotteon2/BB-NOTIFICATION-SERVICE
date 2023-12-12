package kr.bb.notification.domain.notification.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewOrderNotification {
  private String productName;
  private Long storeId;
  private OrderType orderType;
}
