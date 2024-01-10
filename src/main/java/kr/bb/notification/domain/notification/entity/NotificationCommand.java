package kr.bb.notification.domain.notification.entity;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.notification.NotificationKind;
import bloomingblooms.domain.notification.PublishNotificationInformation;
import bloomingblooms.domain.notification.Role;
import bloomingblooms.domain.notification.delivery.DeliveryNotification;
import bloomingblooms.domain.notification.question.InqueryResponseNotification;
import bloomingblooms.domain.order.OrderStatusNotification;
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
        .notificationContent(message.getNotificationKind().getMessage())
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
    private String type;
    private String content;
    private String redirectUrl;
    private String phoneNumber;
    private Role role;
    private NotificationKind notificationKind;
    private String eventId;

    public static List<NotificationInformation> getResaleNotificationData(
        NotificationData<ResaleNotificationList> restoreNotification) {
      return restoreNotification.getWhoToNotify().getResaleNotificationData().stream()
          .map(
              item ->
                  NotificationInformation.builder()
                      .id(item.getUserId())
                      .content(restoreNotification.getPublishInformation().getContent())
                      .type(
                          restoreNotification
                              .getPublishInformation()
                              .getNotificationKind()
                              .getMessage())
                      .phoneNumber(item.getPhoneNumber())
                      .role(restoreNotification.getPublishInformation().getRole())
                      .notificationKind(
                          restoreNotification.getPublishInformation().getNotificationKind())
                      .redirectUrl(restoreNotification.getPublishInformation().getNotificationUrl())
                      .eventId(restoreNotification.getPublishInformation().getEventId())
                      .build())
          .collect(Collectors.toList());
    }

    public static NotificationInformation getSSEData(
        PublishNotificationInformation publishNotificationInformation, Long id) {
      return NotificationInformation.builder()
          .id(id)
          .eventId(publishNotificationInformation.getEventId())
          .role(publishNotificationInformation.getRole())
          .notificationKind(publishNotificationInformation.getNotificationKind())
          .redirectUrl(publishNotificationInformation.getNotificationUrl())
          .type(publishNotificationInformation.getNotificationKind().getMessage())
          .build();
    }

    public static NotificationInformation getDeliveryNotificationData(
        NotificationData<DeliveryNotification> notificationData) {
      return NotificationInformation.builder()
          .eventId(notificationData.getPublishInformation().getEventId())
          .role(notificationData.getPublishInformation().getRole())
          .phoneNumber(notificationData.getWhoToNotify().getPhoneNumber())
          .type(notificationData.getWhoToNotify().getDeliveryStatus().getMessage())
          .redirectUrl(notificationData.getPublishInformation().getNotificationUrl())
          .id(notificationData.getWhoToNotify().getUserId())
          .notificationKind(notificationData.getPublishInformation().getNotificationKind())
          .build();
    }

    public static NotificationInformation getSMSData(
        NotificationData<InqueryResponseNotification> notification) {
      return NotificationInformation.builder()
          .eventId(notification.getPublishInformation().getEventId())
          .notificationKind(notification.getPublishInformation().getNotificationKind())
          .role(notification.getPublishInformation().getRole())
          .id(notification.getWhoToNotify().getUserId())
          .phoneNumber(notification.getWhoToNotify().getPhoneNumber())
          .redirectUrl(notification.getPublishInformation().getNotificationUrl())
          .type(notification.getPublishInformation().getNotificationKind().getMessage())
          .build();
    }

    public static NotificationInformation getNewOrderStatusData(
        NotificationData<OrderStatusNotification> notification) {
      return NotificationInformation.builder()
          .eventId(notification.getPublishInformation().getEventId())
          .notificationKind(notification.getPublishInformation().getNotificationKind())
          .phoneNumber(notification.getWhoToNotify().getPhoneNumber())
          .redirectUrl(notification.getPublishInformation().getNotificationUrl())
          .id(notification.getWhoToNotify().getUserId())
          .type(notification.getPublishInformation().getNotificationKind().getMessage())
          .role(notification.getPublishInformation().getRole())
          .build();
    }
  }
}
