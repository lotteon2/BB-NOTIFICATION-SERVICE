package kr.bb.notification.domain.notification.infrastructure.http.api;

import bloomingblooms.domain.notification.Role;
import bloomingblooms.response.CommonResponse;
import java.util.List;
import kr.bb.notification.domain.notification.application.NotificationCommandService;
import kr.bb.notification.domain.notification.helper.NotificationQueryActionHelper;
import kr.bb.notification.domain.notification.mapper.NotificationCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationQueryRestController {
  private final NotificationQueryActionHelper notificationQueryFacadeHandler;
  private final NotificationCommandService notificationCommandService;

  @GetMapping("/customer")
  public CommonResponse<NotificationCommand.NotificationList> getNotificationsCUSTOMER(
      @RequestHeader Long userId) {
    log.info("userId" + userId);
    return CommonResponse.success(
        notificationQueryFacadeHandler.getNotifications(userId, Role.CUSTOMER));
  }

  @GetMapping("/customer/unread-notification")
  public CommonResponse<NotificationCommand.UnreadNotificationCount>
      getUnreadNotificationCountCUSTOMER(@RequestHeader Long userId) {
    return CommonResponse.success(
        notificationQueryFacadeHandler.getUnreadNotification(userId, Role.CUSTOMER));
  }

  @GetMapping("/manager/{storeId}")
  public CommonResponse<NotificationCommand.NotificationList> getNotificationsMANAGER(
      @PathVariable Long storeId) {
    return CommonResponse.success(
        notificationQueryFacadeHandler.getNotifications(storeId, Role.MANAGER));
  }

  @GetMapping("/manager/{storeId}/unread-notification")
  public CommonResponse<NotificationCommand.UnreadNotificationCount>
      getUnreadNotificationCountMANAGER(@PathVariable Long storeId) {
    return CommonResponse.success(
        notificationQueryFacadeHandler.getUnreadNotification(storeId, Role.MANAGER));
  }

  @GetMapping("/admin")
  public CommonResponse<NotificationCommand.NotificationList> getNotificationsADMIN(
      @RequestHeader Long userId) {
    return CommonResponse.success(
        notificationQueryFacadeHandler.getNotifications(userId, Role.ADMIN));
  }

  @GetMapping("/admin/unread-notification")
  public CommonResponse<NotificationCommand.UnreadNotificationCount>
      getUnreadNotificationCountADMIN(@RequestHeader Long userId) {
    return CommonResponse.success(
        notificationQueryFacadeHandler.getUnreadNotification(userId, Role.ADMIN));
  }

  @PutMapping("customer/check")
  public void customerNotificationCheck(
      @RequestBody List<Long> notificationId, @RequestHeader Long userId) {
    notificationCommandService.updateNotificationIsRead(notificationId, userId, Role.CUSTOMER);
  }

  @PutMapping("admin/check")
  public void adminNotificationCheck(@RequestBody List<Long> notificationId) {
    notificationCommandService.updateNotificationIsRead(notificationId, 100L, Role.ADMIN);
  }

  @PutMapping("manager/{storeId}/check")
  public void managerNotificationCheck(
      @RequestBody List<Long> notificationId, @PathVariable Long storeId) {
    notificationCommandService.updateNotificationIsRead(notificationId, storeId, Role.MANAGER);
  }
}
