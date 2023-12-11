package kr.bb.notification.domain.notification.facade;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.notification.QuestionRegisterNotification;
import bloomingblooms.domain.resale.ResaleNotificationList;
import java.util.List;
import kr.bb.notification.domain.notification.application.NotificationCommandService;
import kr.bb.notification.domain.notification.entity.NotificationCommand.SMSNotification;
import kr.bb.notification.domain.notification.entity.NotificationCommand.SSENotification;
import kr.bb.notification.domain.notification.infrastructure.sms.SendSMS;
import kr.bb.notification.domain.notification.infrastructure.sse.SendSSE;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationFacadeHandler {
  private final SendSMS sms;
  private final SendSSE sse;
  private final NotificationCommandService notificationCommandService;

  public void publishResaleNotification(
      NotificationData<ResaleNotificationList> restoreNotification) {
    List<SMSNotification> data = SMSNotification.getResaleNotificationSMSData(restoreNotification);
    data.forEach(sms::publishCustomer);

    // save notification
    notificationCommandService.saveResaleNotification(restoreNotification);
  }

  public void publishQuestionRegisterNotification(
      NotificationData<QuestionRegisterNotification> questionRegisterNotification) {
    SSENotification sseNotification =
        SSENotification.getQuestionRegisterSSEData(questionRegisterNotification);
    sse.publishCustomer(sseNotification);

    // save notification
    notificationCommandService.saveQuestionRegister(questionRegisterNotification);
  }
}
