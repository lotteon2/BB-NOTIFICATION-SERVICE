package kr.bb.notification.domain.notification.infrastructure.sms;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.resale.ResaleNotificationData;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendSMS {
  private final AmazonSNSAsync amazonSNSAsync;

  public void sendSMS(NotificationData<List<ResaleNotificationData>> data) {
    data.getWhoToNotify()
        .forEach(
            item -> {
              PublishRequest request =
                  new PublishRequest()
                      .withPhoneNumber(item.getPhoneNumber())
                      .withMessage(data.getMessage());

              Future<PublishResult> future = amazonSNSAsync.publishAsync(request);
              try {
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
