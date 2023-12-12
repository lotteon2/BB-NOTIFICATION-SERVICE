package kr.bb.notification.domain.emitter.api;

import kr.bb.notification.domain.emitter.application.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SSERestController {
  private final SseService sseService;

  // TODO: 화면 테스트용으로 pathvariable 사용
  @GetMapping(value = "subscribe/manager/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribeManager(@PathVariable Long userId) {
    return sseService.subscribe(userId, "manager");
  }

  @GetMapping(value = "subscribe/admin/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribeAdmin(@PathVariable Long userId) {
    return sseService.subscribe(userId, "admin");
  }

  @GetMapping(value = "subscribe/customer/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribe(@PathVariable Long userId) {
    return sseService.subscribe(userId, "customer");
  }
}
