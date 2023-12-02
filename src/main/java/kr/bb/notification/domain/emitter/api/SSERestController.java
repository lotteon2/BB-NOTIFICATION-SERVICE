package kr.bb.notification.domain.emitter.api;

import kr.bb.notification.domain.emitter.application.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SSERestController {
  private final SseService sseService;

  @GetMapping(value = "subscribe/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribe(@RequestHeader Long userId) {
    return sseService.subscribe(userId);
  }

  @PostMapping("/send-data/{userId}")
  public void sendData(@PathVariable Long userId, @RequestBody Object data) {
    sseService.notify(userId, data);
  }
}
