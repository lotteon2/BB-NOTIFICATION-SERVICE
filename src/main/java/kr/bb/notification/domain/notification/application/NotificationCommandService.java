package kr.bb.notification.domain.notification.application;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.resale.ResaleNotificationList;
import java.util.List;
import kr.bb.notification.domain.notification.entity.MemberNotification;
import kr.bb.notification.domain.notification.entity.MemberNotificationCommand;
import kr.bb.notification.domain.notification.entity.Notification;
import kr.bb.notification.domain.notification.entity.NotificationCommand;
import kr.bb.notification.domain.notification.repository.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationCommandService {
  private final NotificationJpaRepository notificationJpaRepository;

  @Transactional
  public void saveResaleNotification(NotificationData<ResaleNotificationList> restoreNotification) {
    Notification notification = NotificationCommand.toEntity(restoreNotification.getMessage());
    List<MemberNotification> memberNotifications =
        MemberNotificationCommand.toEntity(restoreNotification.getWhoToNotify());
    notification.setMemberNotifications(memberNotifications);
    notificationJpaRepository.save(notification);
  }
}
