package kr.bb.notification.domain.notification.helper;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.notification.delivery.DeliveryNotification;
import bloomingblooms.domain.notification.neworder.NewOrderNotification;
import bloomingblooms.domain.notification.question.QuestionRegister;
import bloomingblooms.domain.resale.ResaleNotificationList;
import java.util.List;
import kr.bb.notification.domain.notification.application.NotificationCommandService;
import kr.bb.notification.domain.notification.entity.NotificationCommand.NotificationInformation;
import kr.bb.notification.domain.notification.infrastructure.dto.OutOfStockNotification;
import kr.bb.notification.domain.notification.infrastructure.sms.SendSMS;
import kr.bb.notification.domain.notification.infrastructure.sse.SendSSE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationActionHelper {
  private final SendSMS sms;
  private final SendSSE sse;
  private final NotificationCommandService notificationCommandService;

  public void publishResaleNotification(NotificationData<ResaleNotificationList> notification) {
    List<NotificationInformation> data =
        NotificationInformation.getResaleNotificationData(notification);
    data.forEach(
        item -> {
          sms.publishCustomer(item);
          sse.publishCustomer(item);
        });

    // save notification
    notificationCommandService.saveResaleNotification(notification);
  }

  public void publishQuestionRegisterNotification(NotificationData<QuestionRegister> notification) {
    NotificationInformation sseNotification =
        NotificationInformation.getSSEData(
            notification.getPublishInformation(), notification.getWhoToNotify().getStoreId());
    sse.publishCustomer(sseNotification);

    // save notification
    notificationCommandService.saveSingleNotification(
        notification.getPublishInformation(), notification.getWhoToNotify().getStoreId());
  }

  public void publishNewComerNotification(NotificationData<Void> notification) {
    NotificationInformation sseNotification =
        NotificationInformation.getSSEData(notification.getPublishInformation(), 1L);
    sse.publishCustomer(sseNotification);

    // save notification
    notificationCommandService.saveSingleNotification(notification.getPublishInformation(), 1L);
  }

  public void publishNewOrderNotification(NotificationData<NewOrderNotification> notification) {
    NotificationInformation sseNotification =
        NotificationInformation.getSSEData(
            notification.getPublishInformation(), notification.getWhoToNotify().getStoreId());
    sse.publishCustomer(sseNotification);

    // save notification
    notificationCommandService.saveSingleNotification(
        notification.getPublishInformation(), notification.getWhoToNotify().getStoreId());
  }

  public void publishDeliveryStartNotification(
      NotificationData<DeliveryNotification> notificationData) {
    NotificationInformation notifyData =
        NotificationInformation.getDeliveryNotificationData(notificationData);
    sse.publishCustomer(notifyData);
    sms.publishCustomer(notifyData);

    // save notification
    notificationCommandService.saveSingleNotification(
        notificationData.getPublishInformation(), notificationData.getWhoToNotify().getUserId());
  }

  public void publishOutOfStockNotification(
      NotificationData<OutOfStockNotification> outOfStockNotification) {
    NotificationInformation sseData =
        NotificationInformation.getSSEData(
            outOfStockNotification.getPublishInformation(),
            outOfStockNotification.getWhoToNotify().getStoreId());
    sse.publishCustomer(sseData);

    notificationCommandService.saveSingleNotification(
        outOfStockNotification.getPublishInformation(),
        outOfStockNotification.getWhoToNotify().getStoreId());
  }
}
