package kr.bb.notification.entity;

import lombok.Getter;

public class NotificationCommand {
  @Getter
  public enum RedirectUrl {
    PRODUCT_DETAIL("/api/products/%s");
    private final String redirectUrl;

    RedirectUrl(String redirectUrl) {
      this.redirectUrl = redirectUrl;
    }
  }
}
