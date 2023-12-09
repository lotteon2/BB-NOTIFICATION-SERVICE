package kr.bb.notification.domain.notification.entity;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

public class NotificationCommand {
  @Getter
  @Builder
  public static class NotificationItem {
    private Long notificationId;
    private String notificationContent;
    private String notificationLink;
  }

  @Getter
  @Builder
  public static class NotificationList {
    private List<NotificationItem> notifications;

    public static NotificationList getData(List<MemberNotification> notifications) {
      return NotificationList.builder()
          .notifications(
              notifications.stream()
                  .map(
                      item ->
                          NotificationItem.builder()
                              .notificationId(item.getMemberNotificationId())
                              .notificationLink(item.getNotification().getNotificationLink())
                              .notificationContent(item.getNotification().getNotificationContent())
                              .build())
                  .collect(Collectors.toList()))
          .build();
    }
  }
}
