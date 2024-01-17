package kr.bb.notification.domain.notification.infrastructure.action;


import kr.bb.notification.domain.notification.mapper.NotificationCommand;

public interface InfrastructureActionHandler<
    T extends NotificationCommand.NotificationInformation> {
  void publish(T notifyData);
}
