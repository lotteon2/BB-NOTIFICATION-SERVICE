package kr.bb.notification.domain.notification.infrastructure.message;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.notification.NotificationKind;
import bloomingblooms.domain.notification.PublishNotificationInformation;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.bb.notification.domain.notification.infrastructure.dto.NewOrderNotification;
import kr.bb.notification.domain.notification.infrastructure.dto.OrderType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SQSPublishTest {
  private final AmazonSQS sqs;
  private final ObjectMapper objectMapper;

  @Value("${cloud.aws.sqs.new-order-queue.url}")
  private String newOrder;

  public void sqsSend() throws JsonProcessingException {
    NewOrderNotification build =
        NewOrderNotification.builder()
            .orderType(OrderType.SUBSCRIBE)
            .productName("장미 꽃다발")
            .storeId(1L)
            .build();

    SendMessageRequest sendMessageRequest =
        new SendMessageRequest(
            newOrder,
            objectMapper.writeValueAsString(
                NotificationData.builder()
                    .whoToNotify(build)
                    .publishInformation(
                        PublishNotificationInformation.builder()
                            .message(build.getProductName() + " 상품이 주문되었습니다")
                            .notificationUrl("/orders/" + build.getStoreId())
                            .notificationKind(NotificationKind.NEW_ORDER)
                            .build())
                    .build()));
    sqs.sendMessage(sendMessageRequest);
  }
}
