package kr.bb.notification.domain.emitter.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.bb.notification.domain.emitter.application.SseService;
import kr.bb.notification.domain.notification.infrastructure.message.SQSPublishTest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SSEClientController {
  private final SseService sseService;
  private final SQSPublishTest sqsPublishTest;

 @GetMapping("sqs-test")
  public void sqsTest() throws JsonProcessingException {
    sqsPublishTest.sqsSend();
  }
}
