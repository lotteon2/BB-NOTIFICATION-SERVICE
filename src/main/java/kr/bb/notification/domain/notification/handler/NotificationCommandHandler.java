package kr.bb.notification.domain.notification.handler;

import bloomingblooms.domain.resale.ResaleNotificationData;
import bloomingblooms.domain.resale.ResaleNotificationList;
import java.util.List;
import kr.bb.notification.domain.notification.repository.NotificationJpaRepository;
import kr.bb.notification.entity.MemberNotification;
import kr.bb.notification.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationCommandHandler {
  private final NotificationJpaRepository notificationJpaRepository;

  public void save(ResaleNotificationList restoreNotification) {
    List<ResaleNotificationData> resaleNotificationData =
        restoreNotification.getResaleNotificationData();
    Notification notification = Notification.getNotification(restoreNotification);
    List<MemberNotification> resaleNotificationDataList =
        MemberNotification.getMemberNotification(resaleNotificationData);
    notification.setMemberNotifications(resaleNotificationDataList);
    notificationJpaRepository.save(notification);
  }
}
