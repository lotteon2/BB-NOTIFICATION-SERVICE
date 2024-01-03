package kr.bb.notification.domain.notification.api;

import bloomingblooms.domain.notification.Role;
import bloomingblooms.response.CommonResponse;
import kr.bb.notification.domain.notification.entity.NotificationCommand;
import kr.bb.notification.domain.notification.helper.NotificationQueryActionHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationQueryRestController {
  private final NotificationQueryActionHelper notificationQueryFacadeHandler;

  @GetMapping("/customer")
  public CommonResponse<NotificationCommand.NotificationList> getNotificationsCUSTOMER(
      @RequestHeader Long userId) {
    return CommonResponse.success(
        notificationQueryFacadeHandler.getNotifications(userId, Role.CUSTOMER));
  }

  @GetMapping("/customer/unread-notification")
  public CommonResponse<NotificationCommand.UnreadNotificationCount> getUnreadNotificationCountCUSTOMER(
      @RequestHeader Long userId) {
    return CommonResponse.success(
        notificationQueryFacadeHandler.getUnreadNotification(userId, Role.CUSTOMER));
  }

  @GetMapping("/manager")
  public CommonResponse<NotificationCommand.NotificationList> getNotificationsMANAGER(
      @RequestHeader Long userId) {
    return CommonResponse.success(
        notificationQueryFacadeHandler.getNotifications(userId, Role.MANAGER));
  }

  @GetMapping("/manager/unread-notification")
  public CommonResponse<NotificationCommand.UnreadNotificationCount> getUnreadNotificationCountMANAGER(
      @RequestHeader Long userId) {
    return CommonResponse.success(
        notificationQueryFacadeHandler.getUnreadNotification(userId, Role.MANAGER));
  }

  @GetMapping("/admin")
  public CommonResponse<NotificationCommand.NotificationList> getNotificationsADMIN(
      @RequestHeader Long userId) {
    return CommonResponse.success(
        notificationQueryFacadeHandler.getNotifications(userId, Role.ADMIN));
  }

  @GetMapping("/admin/unread-notification")
  public CommonResponse<NotificationCommand.UnreadNotificationCount> getUnreadNotificationCountADMIN(
      @RequestHeader Long userId) {
    return CommonResponse.success(
        notificationQueryFacadeHandler.getUnreadNotification(userId, Role.ADMIN));
  }
}
