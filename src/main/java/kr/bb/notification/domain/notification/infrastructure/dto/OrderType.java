package kr.bb.notification.domain.notification.infrastructure.dto;

import lombok.Getter;

@Getter
public enum OrderType {
  PICKUP("픽업"),
  SUBSCRIBE("구독"),
  DELIVERY("배송");
  private final String orderType;

  OrderType(String orderType) {
    this.orderType = orderType;
  }
}
