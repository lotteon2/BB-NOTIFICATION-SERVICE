package kr.bb.notification.domain.emitter.api;

import bloomingblooms.domain.notification.Role;
import javax.servlet.http.HttpServletResponse;
import kr.bb.notification.domain.emitter.application.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SSERestController {
  private final SseService sseService;

  @GetMapping(value = "subscribe/manager/{storeId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribeManagerLocal(
      @PathVariable Long storeId,
      @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "")
          String lastEventId,
      HttpServletResponse response) {
    response.addHeader("X-Accel-Buffering", "no");
    response.addHeader(HttpHeaders.CONNECTION, "keep-alive");
    response.addHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
    return sseService.subscribe(storeId, Role.MANAGER.getRole());
  }

  @GetMapping(value = "subscribe/admin", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribeAdmin(
      @RequestHeader Long userId,
      @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "")
          String lastEventId,
      HttpServletResponse response) {
    response.addHeader("X-Accel-Buffering", "no");
    response.addHeader(HttpHeaders.CONNECTION, "keep-alive");
    response.addHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
    return sseService.subscribe(userId, Role.ADMIN.getRole());
  }

  @GetMapping(value = "subscribe/customer", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribe(
      @RequestHeader Long userId,
      @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "")
          String lastEventId,
      HttpServletResponse response) {
    response.addHeader("X-Accel-Buffering", "no");
    response.addHeader(HttpHeaders.CONNECTION, "keep-alive");
    response.addHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
    return sseService.subscribe(userId, Role.CUSTOMER.getRole());
  }
}
