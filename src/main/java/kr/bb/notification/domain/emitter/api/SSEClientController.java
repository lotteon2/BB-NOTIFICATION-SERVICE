package kr.bb.notification.domain.emitter.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.bb.notification.domain.emitter.application.SseService;
import kr.bb.notification.domain.notification.entity.NotificationCommand.SSENotification;
import kr.bb.notification.domain.notification.infrastructure.message.SQSPublishTest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SSEClientController {
  private final SseService sseService;
  private final SQSPublishTest sqsPublishTest;

  // TODO: 삭제될 예정, sse 전송 테스트 용
  @PostMapping(value = "/send-data/manager/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public void sendData(@PathVariable Long userId, @RequestBody String data) {
    SSENotification build =
        SSENotification.builder()
            .role("manager")
            .redirectUrl("/question")
            .content(data)
            .userId(3L)
            .build();
    sseService.notify(build);
  }

  @GetMapping("sqs-test")
  public void sqsTest() throws JsonProcessingException {
    sqsPublishTest.sqsSend();
  }
}
