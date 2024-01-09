package kr.bb.notification.domain.notification.application;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.notification.PublishNotificationInformation;
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
@Transactional
@RequiredArgsConstructor
public class NotificationCommandService {
  private final NotificationJpaRepository notificationJpaRepository;

  private Notification getNotification(PublishNotificationInformation publishInformation, Long id) {
    Notification notification = NotificationCommand.toEntity(publishInformation);
    MemberNotification memberNotification =
        MemberNotificationCommand.toEntity(id, publishInformation.getRole(), notification);
    notification.setMemberNotifications(List.of(memberNotification));
    return notification;
  }

  public void saveResaleNotification(NotificationData<ResaleNotificationList> restoreNotification) {
    Notification notification =
        NotificationCommand.toEntity(restoreNotification.getPublishInformation());
    List<MemberNotification> memberNotifications =
        MemberNotificationCommand.toEntityList(restoreNotification.getWhoToNotify(), notification);
    notification.getMemberNotifications().addAll(memberNotifications);
    notificationJpaRepository.save(notification);
  }

  public void saveSingleNotification(PublishNotificationInformation publishInformation, Long id) {
    Notification notification = getNotification(publishInformation, id);
    notificationJpaRepository.save(notification);
  }
}
