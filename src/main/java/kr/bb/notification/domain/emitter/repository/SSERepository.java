package kr.bb.notification.domain.emitter.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
@RequiredArgsConstructor
public class SSERepository {
  private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

  public void save(Long userId, String role, SseEmitter emitter) {
    emitters.put(role + userId, emitter);
  }

  public void deleteById(Long userId, String role) {
    emitters.remove(role + userId);
  }

  public SseEmitter get(Long userId, String role) {
    return emitters.get(role + userId);
  }
}
