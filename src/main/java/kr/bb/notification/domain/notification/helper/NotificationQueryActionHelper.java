package kr.bb.notification.domain.notification.helper;

import bloomingblooms.domain.notification.Role;
import kr.bb.notification.domain.notification.application.NotificationQueryService;
import kr.bb.notification.domain.notification.mapper.NotificationCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationQueryActionHelper {
  private final NotificationQueryService notificationQueryService;

  public NotificationCommand.NotificationList getNotifications(Long userId, Role role) {
    log.info("action handler user id " + userId);
    return notificationQueryService.getNotifications(userId, role);
  }

  public NotificationCommand.UnreadNotificationCount getUnreadNotification(Long userId, Role role) {
    return NotificationCommand.UnreadNotificationCount.getData(
        notificationQueryService.getUnreadNotificationCount(userId, role));
  }
}
