package kr.bb.notification.domain.notification.infrastructure.message;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.notification.PublishNotificationInformation;
import bloomingblooms.domain.notification.Role;
import bloomingblooms.domain.notification.delivery.DeliveryNotification;
import bloomingblooms.domain.notification.newcomer.NewcomerNotification;
import bloomingblooms.domain.notification.order.OrderCancelNotification;
import bloomingblooms.domain.notification.order.SettlementNotification;
import bloomingblooms.domain.notification.question.InqueryResponseNotification;
import bloomingblooms.domain.notification.question.QuestionRegister;
import bloomingblooms.domain.notification.stock.OutOfStockNotification;
import bloomingblooms.domain.order.NewOrderEvent;
import bloomingblooms.domain.order.NewOrderEvent.NewOrderEventItem;
import bloomingblooms.domain.order.OrderStatusNotification;
import bloomingblooms.domain.resale.ResaleNotificationList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import kr.bb.notification.domain.notification.helper.NotificationActionHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationSQSListener {
  private final ObjectMapper objectMapper;
  private final NotificationActionHelper notificationActionHelper;

  /**
   * 상품 재입고 알림
   *
   * @param message
   * @param headers
   * @param ack
   * @throws JsonProcessingException
   */
  @SqsListener(
      value = "${cloud.aws.sqs.product-resale-notification-queue.name}",
      deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void consumeProductResaleNotificationCheckQueue(
      @Payload String message, @Headers Map<String, String> headers, Acknowledgment ack)
      throws JsonProcessingException {
    NotificationData<ResaleNotificationList> restoreNotification =
        objectMapper.readValue(
            message,
            objectMapper
                .getTypeFactory()
                .constructParametricType(NotificationData.class, ResaleNotificationList.class));
    NotificationData<ResaleNotificationList> notification =
        NotificationData.notifyData(
            restoreNotification.getWhoToNotify(),
            PublishNotificationInformation.updateRole(
                restoreNotification.getPublishInformation(), Role.CUSTOMER));

    // call facade
    notificationActionHelper.publishResaleNotification(notification);
    ack.acknowledge();
  }

  /**
   * 문의 등록 알림
   *
   * @param message
   * @param headers
   * @param ack
   * @throws JsonProcessingException
   */
  @SqsListener(
      value = "${cloud.aws.sqs.question-register-notification-queue.name}",
      deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void consumeQuestionRegisterNotificationQueue(
      @Payload String message, @Headers Map<String, String> headers, Acknowledgment ack)
      throws JsonProcessingException {
    NotificationData<QuestionRegister> questionRegisterNotification =
        objectMapper.readValue(
            message,
            objectMapper
                .getTypeFactory()
                .constructParametricType(NotificationData.class, QuestionRegister.class));
    NotificationData<QuestionRegister> notification =
        NotificationData.notifyData(
            questionRegisterNotification.getWhoToNotify(),
            PublishNotificationInformation.updateRole(
                questionRegisterNotification.getPublishInformation(), Role.MANAGER));

    // call facade
    notificationActionHelper.publishQuestionRegisterNotification(notification);
    ack.acknowledge();
  }

  /**
   * 신규 주문 알림 배송 | 픽업 | 구독
   *
   * @param message
   * @param headers
   * @param ack
   * @throws JsonProcessingException
   */
  @SqsListener(
      value = "${cloud.aws.sqs.new-order-queue.name}",
      deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void consumeNewOrderNotificationQueue(
      @Payload String message, @Headers Map<String, String> headers, Acknowledgment ack)
      throws JsonProcessingException {
    // sns 에서 발생한 이벤트 sqs 에서 받기
    String messageFromSNS = getMessageFromSNS(message);
    NewOrderEvent newOrderEvent = objectMapper.readValue(messageFromSNS, NewOrderEvent.class);

    newOrderEvent
        .getOrders()
        .forEach(
            item -> {
              NotificationData<NewOrderEventItem> notificationData =
                  NotificationData.notifyData(
                      item,
                      PublishNotificationInformation.makePublishNotificationInformation(
                          NewOrderEventItem.getNotificationURL(item.getOrderType()),
                          NewOrderEventItem.getNotificationKind(item.getOrderType()),
                          Role.MANAGER));
              notificationActionHelper.publishNewOrderNotification(notificationData);
            });

    ack.acknowledge();
  }

  private String getMessageFromSNS(String message) throws JsonProcessingException {
    JsonNode jsonNode = objectMapper.readTree(message);
    return jsonNode.get("Message").asText();
  }

  /**
   * 신규 회원 가입 심사 알림
   *
   * @param message
   * @param headers
   * @param ack
   * @throws JsonProcessingException
   */
  @SqsListener(
      value = "${cloud.aws.sqs.newcomer-queue.name}",
      deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void consumeNewcomerQueue(
      @Payload String message, @Headers Map<String, String> headers, Acknowledgment ack)
      throws JsonProcessingException {
    NotificationData<Void> newcomer =
        objectMapper.readValue(
            message,
            objectMapper
                .getTypeFactory()
                .constructParametricType(NotificationData.class, NewcomerNotification.class));
    NotificationData<Void> information =
        NotificationData.notifyData(
            newcomer.getWhoToNotify(),
            PublishNotificationInformation.updateRole(
                newcomer.getPublishInformation(), Role.ADMIN));
    // call facade
    notificationActionHelper.publishNewComerNotification(information);
    ack.acknowledge();
  }

  /**
   * 배송 시작 알림
   *
   * @param message
   * @param headers
   * @param ack
   * @throws JsonProcessingException
   */
  @SqsListener(
      value = "${cloud.aws.sqs.delivery-status-update-notification-queue.name}",
      deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void consumeDeliveryStartNotificationQueue(
      @Payload String message, @Headers Map<String, String> headers, Acknowledgment ack)
      throws JsonProcessingException {
    NotificationData<DeliveryNotification> delivery =
        objectMapper.readValue(
            message,
            objectMapper
                .getTypeFactory()
                .constructParametricType(NotificationData.class, DeliveryNotification.class));
    NotificationData<DeliveryNotification> notification =
        NotificationData.notifyData(
            delivery.getWhoToNotify(),
            PublishNotificationInformation.updateRole(
                delivery.getPublishInformation(), Role.CUSTOMER));
    // call facade
    notificationActionHelper.publishDeliveryStartNotification(notification);
    ack.acknowledge();
  }

  /**
   * 정산 알림
   *
   * @param message
   * @param headers
   * @param ack
   * @throws JsonProcessingException
   */
  @SqsListener(
      value = "${cloud.aws.sqs.settlement-notification-queue.name}",
      deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void consumeSettlementNotificationQueue(
      @Payload String message, @Headers Map<String, String> headers, Acknowledgment ack)
      throws JsonProcessingException {
    NotificationData<SettlementNotification> settlement =
        objectMapper.readValue(
            message,
            objectMapper
                .getTypeFactory()
                .constructParametricType(NotificationData.class, SettlementNotification.class));
    NotificationData<SettlementNotification> notification =
        NotificationData.notifyData(
            settlement.getWhoToNotify(),
            PublishNotificationInformation.updateRole(
                settlement.getPublishInformation(), Role.MANAGER));
    // call facade
    notificationActionHelper.publishSettlementNotification(notification);
  }

  /**
   * 꽃 재고 부족 알림
   *
   * @param message
   * @param headers
   * @param ack
   * @throws JsonProcessingException
   */
  @SqsListener(
      value = "${cloud.aws.sqs.out-of-stock-notification-queue.name}",
      deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void consumeOutOfStockNotificationQueue(
      @Payload String message, @Headers Map<String, String> headers, Acknowledgment ack)
      throws JsonProcessingException {
    NotificationData<OutOfStockNotification> outOfStock =
        objectMapper.readValue(
            message,
            objectMapper
                .getTypeFactory()
                .constructParametricType(NotificationData.class, OutOfStockNotification.class));
    NotificationData<OutOfStockNotification> notification =
        NotificationData.notifyData(
            outOfStock.getWhoToNotify(),
            PublishNotificationInformation.updateRole(
                outOfStock.getPublishInformation(), Role.MANAGER));
    // call facade
    notificationActionHelper.publishOutOfStockNotification(notification);
  }

  /**
   * 주문 취소 알림
   *
   * @param message
   * @param headers
   * @param ack
   * @throws JsonProcessingException
   */
  @SqsListener(
      value = "${cloud.aws.sqs.order-cancel-notification-queue.name}",
      deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void consumeOrderCancelNotificationQueue(
      @Payload String message, @Headers Map<String, String> headers, Acknowledgment ack)
      throws JsonProcessingException {
    NotificationData<OrderCancelNotification> orderCancel =
        objectMapper.readValue(
            message,
            objectMapper
                .getTypeFactory()
                .constructParametricType(NotificationData.class, OrderCancelNotification.class));
    NotificationData<OrderCancelNotification> notification =
        NotificationData.notifyData(
            orderCancel.getWhoToNotify(),
            PublishNotificationInformation.updateRole(
                orderCancel.getPublishInformation(), Role.MANAGER));
    // call facade
    notificationActionHelper.publishOrderCancelNotification(notification);
    ack.acknowledge();
  }

  @SqsListener(
      value = "${cloud.aws.sqs.inquery-response-notification-queue.name}",
      deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void consumeInqueryResponseNotificationQueue(
      @Payload String message, @Headers Map<String, String> headers, Acknowledgment ack)
      throws JsonProcessingException {
    NotificationData<InqueryResponseNotification> orderCancel =
        objectMapper.readValue(
            message,
            objectMapper
                .getTypeFactory()
                .constructParametricType(
                    NotificationData.class, InqueryResponseNotification.class));
    NotificationData<InqueryResponseNotification> notification =
        NotificationData.notifyData(
            orderCancel.getWhoToNotify(),
            PublishNotificationInformation.updateRole(
                orderCancel.getPublishInformation(), Role.CUSTOMER));
    // call facade
    notificationActionHelper.publishInqueryResponseNotification(notification);
    ack.acknowledge();
  }
    @SqsListener(
      value = "${cloud.aws.sqs.new-order-status-queue.name}",
      deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void consumeNewOrderStatusQueue(
      @Payload String message, @Headers Map<String, String> headers, Acknowledgment ack)
      throws JsonProcessingException {
    NotificationData<OrderStatusNotification> orderCancel =
        objectMapper.readValue(
            message,
            objectMapper
                .getTypeFactory()
                .constructParametricType(NotificationData.class, OrderStatusNotification.class));
    NotificationData<OrderStatusNotification> notification =
        NotificationData.notifyData(
            orderCancel.getWhoToNotify(),
            PublishNotificationInformation.updateRole(
                orderCancel.getPublishInformation(), Role.CUSTOMER));
    // call facade
    notificationActionHelper.publishNewOrderStatusNotification(notification);
    ack.acknowledge();
  }
}
