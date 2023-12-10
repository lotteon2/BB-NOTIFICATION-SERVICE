package kr.bb.notification.domain.notification.infrastructure.message;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.resale.ResaleNotificationList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import kr.bb.notification.domain.notification.facade.ResaleNotificationFacadeHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationSQSListener {
  private final ObjectMapper objectMapper;
  private final ResaleNotificationFacadeHandler resaleNotificationFacadeHandler;

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
    // call facade
    resaleNotificationFacadeHandler.publishResaleNotification(restoreNotification);
    ack.acknowledge();
  }
}
