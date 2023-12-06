package kr.bb.notification.domain.notification.infrastructure.sms;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.resale.ResaleNotificationData;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendSMS {
  private final AmazonSNSAsync amazonSNSAsync;

  public void sendSMS(NotificationData<List<ResaleNotificationData>> data) {
    // TODO: 메세지 v2로 변경 고려해보기
    data.getWhoToNotify()
        .forEach(
            item -> {
              Map<String, MessageAttributeValue> smsAttributes =
                  new HashMap<String, MessageAttributeValue>();
              smsAttributes.put(
                  "AWS.SNS.SMS.SenderID",
                  new MessageAttributeValue()
                      .withStringValue("bb") // The sender ID shown on the device.
                      .withDataType("String"));
              smsAttributes.put(
                  "AWS.SNS.SMS.MaxPrice",
                  new MessageAttributeValue()
                      .withStringValue("0.50") // Sets the max price to 0.50 USD.
                      .withDataType("Number"));
              smsAttributes.put(
                  "AWS.SNS.SMS.SMSType",
                  new MessageAttributeValue()
                      .withStringValue("Promotional") // Sets the type to promotional.
                      .withDataType("String"));
              PublishRequest request =
                  new PublishRequest()
                      .withPhoneNumber(item.getPhoneNumber())
                      .withMessage(data.getMessage())
                      .withMessageAttributes(smsAttributes);

              try {
                Future<PublishResult> future = amazonSNSAsync.publishAsync(request);
                PublishResult publishResult = future.get();
                System.out.printf(
                    "message to : %s%n%s", item.getPhoneNumber(), publishResult.getMessageId());
              } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
              }
            });
    amazonSNSAsync.shutdown();
  }
}
