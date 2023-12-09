package kr.bb.notification.domain.notification.application;

import kr.bb.notification.domain.notification.repository.MemberNotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryService {
  private final MemberNotificationJpaRepository memberNotificationJpaRepository;

  public Long getUnreadNotificationCount(Long userId) {
    return memberNotificationJpaRepository.findUnreadNotificationCount(userId);
  }
}
