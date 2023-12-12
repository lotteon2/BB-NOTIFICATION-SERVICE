package kr.bb.notification.domain.notification.application;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.notification.QuestionRegisterNotification;
import bloomingblooms.domain.resale.ResaleNotificationList;
import java.util.List;
import kr.bb.notification.domain.notification.entity.MemberNotification;
import kr.bb.notification.domain.notification.entity.MemberNotificationCommand;
import kr.bb.notification.domain.notification.entity.Notification;
import kr.bb.notification.domain.notification.entity.NotificationCommand;
import kr.bb.notification.domain.notification.infrastructure.dto.NewOrderNotification;
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

  public void saveQuestionRegister(
      NotificationData<QuestionRegisterNotification> questionRegisterNotification) {
    Notification notification =
        NotificationCommand.toEntity(questionRegisterNotification.getPublishInformation());
    MemberNotification memberNotification =
        MemberNotificationCommand.toEntity(questionRegisterNotification.getWhoToNotify());
    notification.setMemberNotifications(List.of(memberNotification));
    notificationJpaRepository.save(notification);
  }

  public void saveNewOrderNotification(
      NotificationData<NewOrderNotification> newOrderNotification) {
    Notification notification =
        NotificationCommand.toEntity(newOrderNotification.getPublishInformation());
    MemberNotification entityForStoreManager =
        MemberNotificationCommand.toEntityForStoreManager(newOrderNotification.getWhoToNotify());
    notification.setMemberNotifications(List.of(entityForStoreManager));
    notificationJpaRepository.save(notification);
  }
}
