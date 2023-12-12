package kr.bb.notification.domain.notification.application;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.notification.PublishNotificationInformation;
import bloomingblooms.domain.notification.Role;
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

  public void saveResaleNotification(NotificationData<ResaleNotificationList> restoreNotification) {
    Notification notification =
        NotificationCommand.toEntity(restoreNotification.getPublishInformation());
    List<MemberNotification> memberNotifications =
        MemberNotificationCommand.toEntityList(restoreNotification.getWhoToNotify());
    notification.setMemberNotifications(memberNotifications);
    notificationJpaRepository.save(notification);
  }

  public void saveManagerNotification(
      PublishNotificationInformation publishNotificationInformation, Long storeId) {
    Notification notification = NotificationCommand.toEntity(publishNotificationInformation);
    MemberNotification memberNotification =
        MemberNotificationCommand.toEntity(storeId, Role.MANAGER);
    notification.setMemberNotifications(List.of(memberNotification));
    notificationJpaRepository.save(notification);
  }

  public void saveNewcomerNotification(
      PublishNotificationInformation publishInformation, Long userId) {
    Notification entity = NotificationCommand.toEntity(publishInformation);
    MemberNotification memberNotification = MemberNotificationCommand.toEntity(userId, Role.ADMIN);
    entity.setMemberNotifications(List.of(memberNotification));
    notificationJpaRepository.save(entity);
  }
}
