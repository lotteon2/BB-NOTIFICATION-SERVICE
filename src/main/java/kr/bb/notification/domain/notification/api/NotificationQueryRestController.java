package kr.bb.notification.domain.notification.api;

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

  @GetMapping("/")
  public CommonResponse<NotificationCommand.NotificationList> getNotifications(
      @RequestHeader Long userId) {
    return CommonResponse.success(notificationQueryFacadeHandler.getNotifications(userId));
  }
}
