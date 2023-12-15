package kr.bb.notification.domain.notification.application;

import java.util.List;
import java.util.stream.Collectors;
import kr.bb.notification.domain.notification.entity.MemberNotification;
import kr.bb.notification.domain.notification.entity.NotificationCommand;
import kr.bb.notification.domain.notification.repository.MemberNotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryService {
  private final MemberNotificationJpaRepository memberNotificationJpaRepository;

  @Transactional
  public NotificationCommand.NotificationList getNotifications(Long userId) {
    List<MemberNotification> notifications =
        memberNotificationJpaRepository.findNotifications(userId);
    List<MemberNotification> notificationsIsRead =
        notifications.stream().map(MemberNotification::updateIsRead).collect(Collectors.toList());
    return NotificationCommand.NotificationList.getData(notificationsIsRead);
  }

  public Long getUnreadNotificationCount(Long userId) {
    return memberNotificationJpaRepository.findUnreadNotificationCount(userId);
  }
}
