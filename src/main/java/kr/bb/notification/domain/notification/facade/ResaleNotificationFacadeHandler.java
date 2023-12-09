package kr.bb.notification.domain.notification.facade;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.resale.ResaleNotificationList;
import java.util.List;
import kr.bb.notification.domain.notification.application.NotificationCommandService;
import kr.bb.notification.domain.notification.infrastructure.sms.SendSMS;
import kr.bb.notification.entity.NotificationCommand.SMSNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResaleNotificationFacadeHandler {
  private final SendSMS sms;
  private final NotificationCommandService notificationCommandService;

  public void publishResaleNotification(
      NotificationData<ResaleNotificationList> restoreNotification) {
    List<SMSNotification> data = SMSNotification.getData(restoreNotification);
    data.forEach(sms::publishCustomer);

    // save notification
    notificationCommandService.saveResaleNotification(restoreNotification);
  }
}
