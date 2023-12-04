package kr.bb.notification.domain.emitter.application;

import java.io.IOException;
import kr.bb.notification.domain.emitter.repository.SSERepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class SseService {
  private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
  private final SSERepository emitterRepository;

  public SseEmitter subscribe(Long userId) {
    SseEmitter emitter = createEmitter(userId);
    sendToClient(userId, "EventStream Created. [userId=" + userId + "]");
    return emitter;
  }

  public void notify(Long userId, Object event) {
    sendToClient(userId, event);
  }

  private void sendToClient(Long userId, Object data) {
    SseEmitter emitter = emitterRepository.get(userId);
    if (emitter != null) {
      try {
        emitter.send(SseEmitter.event().id(String.valueOf(userId)).name("sse").data(data));
      } catch (IOException exception) {
        emitterRepository.deleteById(userId);
        throw new RuntimeException("연결 오류!");
      }
    }
  }

  private SseEmitter createEmitter(Long userId) {
    SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
    emitterRepository.save(userId, emitter);

    // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.
    emitter.onCompletion(() -> emitterRepository.deleteById(userId));
    // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
    emitter.onTimeout(() -> emitterRepository.deleteById(userId));

    return emitter;
  }
}
