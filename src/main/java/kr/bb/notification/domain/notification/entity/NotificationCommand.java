package kr.bb.notification.domain.notification.entity;

import lombok.Builder;
import lombok.Getter;

public class NotificationCommand {
  @Getter
  @Builder
  public static class UnreadNotificationCount {
    private Long unreadCount;

    public static UnreadNotificationCount getData(Long unreadNotificationCount) {
      return UnreadNotificationCount.builder().unreadCount(unreadNotificationCount).build();
    }
  }
}
