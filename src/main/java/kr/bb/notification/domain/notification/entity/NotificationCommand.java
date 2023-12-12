package kr.bb.notification.domain.notification.entity;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.notification.PublishNotificationInformation;
import bloomingblooms.domain.notification.Role;
import bloomingblooms.domain.resale.ResaleNotificationList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NotificationCommand {
  public static Notification toEntity(PublishNotificationInformation message) {
    return Notification.builder()
        .notificationContent(message.getNotificationKind().getKind())
        .notificationLink(message.getNotificationUrl())
        .build();
  }

  @Getter
  public enum RedirectUrl {
    PRODUCT_DETAIL("/products/");
    private final String redirectUrl;

    RedirectUrl(String redirectUrl) {
      this.redirectUrl = redirectUrl;
    }
  }

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

  @Getter
  @Builder
  public static class UnreadNotificationCount {
    private Long unreadCount;

    public static UnreadNotificationCount getData(Long unreadNotificationCount) {
      return UnreadNotificationCount.builder().unreadCount(unreadNotificationCount).build();
    }
  }

  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor
  public static class NotificationInformation {
    private Long id;
    private String content;
    private String redirectUrl;
    private String phoneNumber;
    private Role role;

    public static List<NotificationInformation> getResaleNotificationData(
        NotificationData<ResaleNotificationList> restoreNotification) {
      return restoreNotification.getWhoToNotify().getResaleNotificationData().stream()
          .map(
              item ->
                  NotificationInformation.builder()
                      .id(item.getUserId())
                      .content(
                          restoreNotification
                              .getPublishInformation()
                              .getNotificationKind()
                              .getKind())
                      .phoneNumber(item.getPhoneNumber())
                      .role(Role.CUSTOMER)
                      .redirectUrl(restoreNotification.getPublishInformation().getNotificationUrl())
                      .build())
          .collect(Collectors.toList());
    }

    public static NotificationInformation getNewComerSSEData(
        PublishNotificationInformation publishNotificationInformation, Long adminId) {
      return NotificationInformation.builder()
          .role(Role.ADMIN)
          .redirectUrl(publishNotificationInformation.getNotificationUrl())
          .id(adminId)
          .build();
    }

    public static NotificationInformation getSSEToManager(
        PublishNotificationInformation publishNotificationInformation, Long storeId) {
      return NotificationInformation.builder()
          .id(storeId)
          .role(Role.MANAGER)
          .redirectUrl(publishNotificationInformation.getNotificationUrl())
          .content(publishNotificationInformation.getNotificationKind().getKind())
          .build();
    }
  }
}
