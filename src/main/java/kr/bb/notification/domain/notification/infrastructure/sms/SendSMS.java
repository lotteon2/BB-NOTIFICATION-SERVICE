package kr.bb.notification.domain.notification.infrastructure.sms;

import com.amazonaws.services.sns.AmazonSNSAsync;
import java.util.List;
import kr.bb.notification.config.AWSConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendSMS {
  private final AmazonSNSAsync amazonSNSAsync;

  public <T> void send(List<T> notifyInformation, String message) {
    amazonSNSAsync.publish
  }
}
