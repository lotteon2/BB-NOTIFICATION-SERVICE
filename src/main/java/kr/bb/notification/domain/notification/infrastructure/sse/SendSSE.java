package kr.bb.notification.domain.notification.infrastructure.sse;

import kr.bb.notification.common.annotation.DuplicateEventCheck;
import kr.bb.notification.domain.emitter.application.SseService;
import kr.bb.notification.domain.notification.mapper.NotificationCommand.NotificationInformation;
import kr.bb.notification.domain.notification.infrastructure.action.InfrastructureActionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendSSE implements InfrastructureActionHandler<NotificationInformation> {
  private final SseService sseService;

  @DuplicateEventCheck(getEventType = "sse")
  @Override
  public void publish(NotificationInformation notifyData) {
    sseService.notify(notifyData);
  }
}
