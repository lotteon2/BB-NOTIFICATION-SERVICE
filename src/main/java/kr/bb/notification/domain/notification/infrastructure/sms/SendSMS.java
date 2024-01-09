package kr.bb.notification.domain.notification.infrastructure.sms;

import java.util.HashMap;
import java.util.Map;
import kr.bb.notification.common.annotation.DuplicateEventHandleAnnotation;
import kr.bb.notification.config.AWSConfiguration;
import kr.bb.notification.domain.notification.entity.NotificationCommand.NotificationInformation;
import kr.bb.notification.domain.notification.infrastructure.action.InfrastructureActionHandler;
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
public class SendSMS implements InfrastructureActionHandler<NotificationInformation> {
  private final AWSConfiguration awsConfiguration;

  private static PublishRequest makePublishRequest(NotificationInformation notifyData) {
    if (notifyData.getContent() == null)
      return PublishRequest.builder()
          .message(notifyData.getType() + "\n" + notifyData.getRedirectUrl())
          .phoneNumber("+82" + notifyData.getPhoneNumber())
          .build();
    else
      return PublishRequest.builder()
          .message(
              notifyData.getType()
                  + "\n"
                  + notifyData.getContent()
                  + "\n"
                  + notifyData.getRedirectUrl())
          .phoneNumber("+82" + notifyData.getPhoneNumber())
          .build();
  }

  private static void setSMSAttribute(SnsClient snsClient) {
    Map<String, String> attributes = new HashMap<>();
    attributes.put("DefaultSMSType", "Promotional");
    SetSmsAttributesRequest request =
        SetSmsAttributesRequest.builder().attributes(attributes).build();
    snsClient.setSMSAttributes(request);
  }

  @DuplicateEventHandleAnnotation(getEventType = "sms")
  @Override
  public void publish(NotificationInformation notifyData) {
    SnsClient snsClient = awsConfiguration.snsClient();
    try {
      setSMSAttribute(snsClient);
      PublishRequest publishRequest = makePublishRequest(notifyData);
      PublishResponse response = snsClient.publish(publishRequest);
      log.info(response.messageId() + " message sent " + response.sdkHttpResponse().statusCode());
    } catch (SnsException e) {
      log.error(notifyData.getNotificationKind() + "알림 메세지 전송 실패");
    }
  }
}
