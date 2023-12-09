package kr.bb.notification.domain.notification.api;

import bloomingblooms.response.CommonResponse;
import kr.bb.notification.domain.notification.entity.NotificationCommand;
import kr.bb.notification.domain.notification.facade.NotificationQueryFacadeHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationQueryController {
  private final NotificationQueryFacadeHandler notificationQueryFacadeHandler;

  @GetMapping("unread-notification")
  public CommonResponse<NotificationCommand.UnreadNotificationCount> getUnreadNotificationCount(
      @RequestHeader Long userId) {
    return CommonResponse.success(notificationQueryFacadeHandler.getUnreadNotification(userId));
  }
}
