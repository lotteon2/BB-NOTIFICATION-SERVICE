package kr.bb.notification.domain.emitter.application;

import java.io.IOException;
import kr.bb.notification.domain.emitter.repository.SSERepository;
import kr.bb.notification.domain.notification.mapper.NotificationCommand.NotificationInformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {
  private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
  private final SSERepository emitterRepository;

  public SseEmitter subscribe(Long userId, String role) {
    SseEmitter emitter = createEmitter(userId, role);
    log.info("subscribe user : [" + role + userId + "]");
    sendToClient(userId, role, "EventStream Created. [userId=" + userId + "]");
    return emitter;
  }

  public void notify(NotificationInformation event) {
    sendToClient(event);
  }

  private void sendToClient(NotificationInformation event) {
    String id = event.getRole().getRole() + event.getId();
    SseEmitter emitter = emitterRepository.get(event.getId(), event.getRole().getRole());
    if (emitter != null) {
      try {
        log.info("send sse event to user : [" + id + "]");
        emitter.send(
            SseEmitter.event()
                .id(id)
                .data(event.getNotificationKind())
                .name(String.valueOf(event.getNotificationKind())));
      } catch (IOException e) {
        log.error(e.getMessage());
        throw new RuntimeException(e);
      }
    }
  }

  private void sendToClient(Long userId, String role, Object data) {
    SseEmitter emitter = emitterRepository.get(userId, role);

    if (emitter != null) {
      try {
        emitter.send(SseEmitter.event().id(String.valueOf(userId)).name("CONNECT").data(data));
      } catch (IOException exception) {
        emitterRepository.deleteById(userId, role);
        throw new RuntimeException("연결 오류!");
      }
    }
  }

  private SseEmitter createEmitter(Long userId, String role) {
    SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
    emitterRepository.save(userId, role, emitter);

    // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.
    emitter.onCompletion(() -> emitterRepository.deleteById(userId, role));
    // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
    emitter.onTimeout(() -> emitterRepository.deleteById(userId, role));

    return emitter;
  }
}
