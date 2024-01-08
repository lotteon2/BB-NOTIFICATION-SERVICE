package kr.bb.notification.domain.notification.helper;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.notification.delivery.DeliveryNotification;
import bloomingblooms.domain.notification.order.OrderCancelNotification;
import bloomingblooms.domain.notification.order.SettlementNotification;
import bloomingblooms.domain.notification.question.InqueryResponseNotification;
import bloomingblooms.domain.notification.question.QuestionRegister;
import bloomingblooms.domain.notification.stock.OutOfStockNotification;
import bloomingblooms.domain.order.NewOrderEvent.NewOrderEventItem;
import bloomingblooms.domain.order.OrderStatusNotification;
import bloomingblooms.domain.resale.ResaleNotificationList;
import java.util.List;
import kr.bb.notification.domain.notification.application.NotificationCommandService;
import kr.bb.notification.domain.notification.entity.NotificationCommand.NotificationInformation;
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
                    sms.publish(item);
                    sse.publish(item);
                });

        // save notification
        notificationCommandService.saveResaleNotification(notification);
    }

    public void publishQuestionRegisterNotification(
            NotificationData<QuestionRegister> notification) {
        NotificationInformation sseNotification =
                NotificationInformation.getSSEData(
                        notification.getPublishInformation(),
                        notification.getWhoToNotify().getStoreId());
        sse.publish(sseNotification);

        // save notification
        notificationCommandService.saveSingleNotification(
                notification.getPublishInformation(), notification.getWhoToNotify().getStoreId());
    }

    public void publishNewComerNotification(NotificationData<Void> notification) {
        NotificationInformation sseNotification =
                NotificationInformation.getSSEData(notification.getPublishInformation(), 100L);
        sse.publish(sseNotification);

        // save notification
        notificationCommandService.saveSingleNotification(notification.getPublishInformation(), 100L);
    }

    public void publishNewOrderNotification(NotificationData<NewOrderEventItem> notification) {
        NotificationInformation sseNotification =
                NotificationInformation.getSSEData(
                        notification.getPublishInformation(),
                        notification.getWhoToNotify().getStoreId());
        sse.publish(sseNotification);

        // save notification
        notificationCommandService.saveSingleNotification(
                notification.getPublishInformation(), notification.getWhoToNotify().getStoreId());
    }

    public void publishOutOfStockNotification(
            NotificationData<OutOfStockNotification> outOfStockNotification) {
        NotificationInformation sseData =
                NotificationInformation.getSSEData(
                        outOfStockNotification.getPublishInformation(),
                        outOfStockNotification.getWhoToNotify().getStoreId());
        sse.publish(sseData);

        notificationCommandService.saveSingleNotification(
                outOfStockNotification.getPublishInformation(),
                outOfStockNotification.getWhoToNotify().getStoreId());
    }

    public void publishDeliveryStartNotification(
            NotificationData<DeliveryNotification> notificationData) {
        NotificationInformation notifyData =
                NotificationInformation.getDeliveryNotificationData(notificationData);
        sse.publish(notifyData);
        sms.publish(notifyData);

        // save notification
        notificationCommandService.saveSingleNotification(
                notificationData.getPublishInformation(),
                notificationData.getWhoToNotify().getUserId());
    }

    public void publishSettlementNotification(
            NotificationData<SettlementNotification> notification) {
        NotificationInformation sseData =
                NotificationInformation.getSSEData(
                        notification.getPublishInformation(),
                        notification.getWhoToNotify().getStoreId());
        sse.publish(sseData);

        notificationCommandService.saveSingleNotification(
                notification.getPublishInformation(), notification.getWhoToNotify().getStoreId());
    }

    public void publishOrderCancelNotification(
            NotificationData<OrderCancelNotification> notification) {
        NotificationInformation sseData =
                NotificationInformation.getSSEData(
                        notification.getPublishInformation(),
                        notification.getWhoToNotify().getStoreId());
        sse.publish(sseData);

        notificationCommandService.saveSingleNotification(
                notification.getPublishInformation(), notification.getWhoToNotify().getStoreId());
    }

    /**
     * 문의 답변 등록 알림
     *
     * @param notification
     */
    public void publishInqueryResponseNotification(
            NotificationData<InqueryResponseNotification> notification) {
        NotificationInformation sseData =
                NotificationInformation.getSSEData(
                        notification.getPublishInformation(),
                        notification.getWhoToNotify().getUserId());
        NotificationInformation smsData = NotificationInformation.getSMSData(notification);
        sse.publish(sseData);
        sms.publish(smsData);

        // save notification
        notificationCommandService.saveSingleNotification(
                notification.getPublishInformation(), notification.getWhoToNotify().getUserId());
    }

    /**
     * 주문 상태 알림
     *
     * @param notification
     */
    public void publishNewOrderStatusNotification(
            NotificationData<OrderStatusNotification> notification) {
        NotificationInformation sseData =
                NotificationInformation.getSSEData(
                        notification.getPublishInformation(),
                        notification.getWhoToNotify().getUserId());
        NotificationInformation smsData = NotificationInformation.getNewOrderStatusData(
                notification);
        sse.publish(sseData);
        sms.publish(smsData);

        // save notification
        notificationCommandService.saveSingleNotification(
                notification.getPublishInformation(), notification.getWhoToNotify().getUserId());
    }
}
