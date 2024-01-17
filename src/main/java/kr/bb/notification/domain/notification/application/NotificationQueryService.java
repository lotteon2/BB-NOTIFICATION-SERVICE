package kr.bb.notification.domain.notification.application;

import bloomingblooms.domain.notification.Role;
import java.util.List;
import java.util.stream.Collectors;
import kr.bb.notification.domain.notification.entity.MemberNotification;
import kr.bb.notification.domain.notification.mapper.NotificationCommand;
import kr.bb.notification.domain.notification.repository.MemberNotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryService {
  private final MemberNotificationJpaRepository memberNotificationJpaRepository;

  public NotificationCommand.NotificationList getNotifications(Long userId, Role role) {

    List<MemberNotification> notifications =
        memberNotificationJpaRepository.findNotifications(userId, role);
    return NotificationCommand.NotificationList.getData(notifications);
  }

  public Long getUnreadNotificationCount(Long userId, Role role) {
    return memberNotificationJpaRepository.findUnreadNotificationCount(userId, role);
  }
}
