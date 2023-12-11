package kr.bb.notification.domain.notification.infrastructure.sse;

import kr.bb.notification.domain.emitter.application.SseService;
import kr.bb.notification.domain.notification.entity.NotificationCommand.SSENotification;
import kr.bb.notification.domain.notification.infrastructure.action.InfrastructureActionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendSSE implements InfrastructureActionHandler<SSENotification> {
  private final SseService sseService;

  @Override
  public void publishCustomer(SSENotification notifyData) {
    sseService.notify(notifyData);
  }
}
