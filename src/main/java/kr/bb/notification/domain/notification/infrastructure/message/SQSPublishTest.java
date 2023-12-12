package kr.bb.notification.domain.notification.infrastructure.message;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.notification.NotificationKind;
import bloomingblooms.domain.notification.PublishNotificationInformation;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SQSPublishTest {
  private final AmazonSQS sqs;
  private final ObjectMapper objectMapper;

  @Value("${cloud.aws.sqs.newcomer-queue.url}")
  private String newOrder;

  public void sqsSend() throws JsonProcessingException {
    SendMessageRequest sendMessageRequest =
        new SendMessageRequest(
            newOrder,
            objectMapper.writeValueAsString(
                NotificationData.builder()
                    .publishInformation(
                        PublishNotificationInformation.builder()
                            .message("회원 가입 승인 요청입니다")
                            .notificationUrl("/admin/")
                            .notificationKind(NotificationKind.NEW_ORDER)
                            .build())
                    .build()));
    sqs.sendMessage(sendMessageRequest);
  }
}
