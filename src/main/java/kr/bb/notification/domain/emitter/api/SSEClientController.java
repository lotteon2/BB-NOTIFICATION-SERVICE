package kr.bb.notification.domain.emitter.api;

import kr.bb.notification.domain.emitter.application.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SSEClientController {
  private final SseService sseService;

  @PostMapping("/send-data/{userId}")
  public void sendData(@PathVariable Long userId, @RequestBody Object data) {
    sseService.notify(userId, data);
  }
}
