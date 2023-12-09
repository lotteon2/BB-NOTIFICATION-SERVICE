package kr.bb.notification.domain.notification.infrastructure.sms;

import java.util.HashMap;
import java.util.Map;
import kr.bb.notification.config.AWSConfiguration;
import kr.bb.notification.domain.notification.infrastructure.action.InfrastructureActionHandler;
import kr.bb.notification.entity.NotificationCommand;
import kr.bb.notification.entity.NotificationCommand.SMSNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SetSmsAttributesRequest;
import software.amazon.awssdk.services.sns.model.SnsException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendSMS implements InfrastructureActionHandler<SMSNotification> {
  private final AWSConfiguration awsConfiguration;

  private static PublishRequest makePublishRequest(SMSNotification notifyData) {
    return PublishRequest.builder()
        .message(notifyData.getContent())
        .phoneNumber(notifyData.getPhoneNumber())
        .build();
  }

  private static void setSMSAttribute(SnsClient snsClient) {
    Map<String, String> attributes = new HashMap<>();
    attributes.put("DefaultSMSType", "Promotional");
    SetSmsAttributesRequest request =
        SetSmsAttributesRequest.builder().attributes(attributes).build();
    snsClient.setSMSAttributes(request);
  }

  @Override
  public NotificationCommand.SMSNotification publishCustomer(
      NotificationCommand.SMSNotification notifyData) {
    SnsClient snsClient = awsConfiguration.snsClient();
    try {
      setSMSAttribute(snsClient);
      PublishRequest publishRequest = makePublishRequest(notifyData);
      PublishResponse response = snsClient.publish(publishRequest);
      log.info(response.messageId() + " message sent " + response.sdkHttpResponse().statusCode());
    } catch (SnsException e) {
      log.error("재입고 알림 메세지 전송 실패");
    }
    return notifyData;
  }
}
