package kr.bb.notification.domain.notification.application;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.notification.PublishNotificationInformation;
import bloomingblooms.domain.notification.Role;
import bloomingblooms.domain.resale.ResaleNotificationList;
import java.util.List;
import java.util.stream.Collectors;
import kr.bb.notification.domain.notification.entity.MemberNotification;
import kr.bb.notification.domain.notification.entity.Notification;
import kr.bb.notification.domain.notification.mapper.MemberNotificationCommand;
import kr.bb.notification.domain.notification.mapper.NotificationCommand;
import kr.bb.notification.domain.notification.repository.MemberNotificationJpaRepository;
import kr.bb.notification.domain.notification.repository.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationCommandService {
  private final NotificationJpaRepository notificationJpaRepository;
  private final MemberNotificationJpaRepository memberNotificationJpaRepository;

  private Notification getNotification(PublishNotificationInformation publishInformation, Long id) {
    Notification notification = NotificationCommand.toEntity(publishInformation);
    MemberNotification memberNotification =
        MemberNotificationCommand.toEntity(id, publishInformation.getRole(), notification);
    notification.setMemberNotifications(List.of(memberNotification));
    return notification;
  }

  public void saveResaleNotification(NotificationData<ResaleNotificationList> restoreNotification) {
    Notification notification =
        NotificationCommand.toEntity(
            restoreNotification.getPublishInformation(),
            restoreNotification.getWhoToNotify().getProductId());
    List<MemberNotification> memberNotifications =
        MemberNotificationCommand.toEntityList(restoreNotification.getWhoToNotify(), notification);
    notification.getMemberNotifications().addAll(memberNotifications);
    notificationJpaRepository.save(notification);
  }

  public void saveSingleNotification(PublishNotificationInformation publishInformation, Long id) {
    Notification notification = getNotification(publishInformation, id);
    notificationJpaRepository.save(notification);
  }

  public void updateNotificationIsRead(List<Long> notificationId, Long userId, Role role) {
    memberNotificationJpaRepository.updateNotificationIsRead(notificationId, userId, role);
  }

  public void saveMultipleNotification(
      PublishNotificationInformation publishInformation, List<Long> storeIdList) {
    Notification notification = NotificationCommand.toEntity(publishInformation);
    List<MemberNotification> memberNotifications =
        storeIdList.stream()
            .map(
                item ->
                    MemberNotificationCommand.toEntity(
                        1L, publishInformation.getRole(), notification))
            .collect(Collectors.toList());
    notification.setMemberNotifications(memberNotifications);
    notificationJpaRepository.save(notification);
  }
}
