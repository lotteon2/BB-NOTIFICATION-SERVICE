package kr.bb.notification.domain.notification.entity;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.notification.PublishNotificationInformation;
import bloomingblooms.domain.resale.ResaleNotificationList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

public class NotificationCommand {
  public static Notification toEntity(PublishNotificationInformation message) {
    return Notification.builder()
        .notificationContent(message.getMessage())
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
  public static class UnreadNotificationCount {
    private Long unreadCount;

    public static UnreadNotificationCount getData(Long unreadNotificationCount) {
      return UnreadNotificationCount.builder().unreadCount(unreadNotificationCount).build();}}

  @Getter
  public static class NotificationInformation {
    private final Long userId;
    private final String content;
    private final String redirectUrl;

    @Builder(builderMethodName = "parentBuilder")
    protected NotificationInformation(Long userId, String content, String redirectUrl) {
      this.userId = userId;
      this.content = content;
      this.redirectUrl = redirectUrl;
    }
  }

  @Getter
  public static class SMSNotification extends NotificationInformation {
    private final String phoneNumber;

    @Builder
    public SMSNotification(Long userId, String content, String redirectUrl, String phoneNumber) {
      super(userId, content, redirectUrl);
      this.phoneNumber = phoneNumber;
    }

    public static List<SMSNotification> getData(
        NotificationData<ResaleNotificationList> restoreNotification) {
      return restoreNotification.getWhoToNotify().getResaleNotificationData().stream()
          .map(
              item ->
                  SMSNotification.builder()
                      .phoneNumber(item.getPhoneNumber())
                      .content(
                          restoreNotification.getMessage().getNotificationKind().getKind()
                              + "\n"
                              + restoreNotification.getWhoToNotify().getProductName()
                              + restoreNotification.getMessage().getMessage())
                      .redirectUrl(
                          RedirectUrl.PRODUCT_DETAIL.getRedirectUrl()
                              + restoreNotification.getWhoToNotify().getProductId())
                      .userId(item.getUserId())
                      .build())
          .collect(Collectors.toList());
    }

    public static SMSNotificationBuilder builder() {
      return new SMSNotificationBuilder();
    }

    public static class SMSNotificationBuilder extends NotificationInformationBuilder {
      private String phoneNumber;

      public SMSNotificationBuilder phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
      }

      @Override
      public SMSNotification build() {
        return new SMSNotification(userId, content, redirectUrl, phoneNumber);
      }
    }
  }
}
