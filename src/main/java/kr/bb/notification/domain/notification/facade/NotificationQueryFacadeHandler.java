package kr.bb.notification.domain.notification.facade;

import kr.bb.notification.domain.notification.application.NotificationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationQueryFacadeHandler {
  private final NotificationQueryService notificationQueryService;

  public Long getUnreadNotification(Long userId) {
    return notificationQueryService.getUnreadNotificationCount(userId);
  }
}
