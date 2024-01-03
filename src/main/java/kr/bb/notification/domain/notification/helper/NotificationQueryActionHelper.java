package kr.bb.notification.domain.notification.helper;

import bloomingblooms.domain.notification.Role;
import kr.bb.notification.domain.notification.application.NotificationQueryService;
import kr.bb.notification.domain.notification.entity.NotificationCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationQueryActionHelper {
  private final NotificationQueryService notificationQueryService;

  public NotificationCommand.NotificationList getNotifications(Long userId, Role role) {
    return notificationQueryService.getNotifications(userId, role);
  }

  public NotificationCommand.UnreadNotificationCount getUnreadNotification(Long userId, Role role) {
    return NotificationCommand.UnreadNotificationCount.getData(
        notificationQueryService.getUnreadNotificationCount(userId, role));
  }
}
