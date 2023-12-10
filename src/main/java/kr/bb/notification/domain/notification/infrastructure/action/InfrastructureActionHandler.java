package kr.bb.notification.domain.notification.infrastructure.action;

import kr.bb.notification.entity.NotificationCommand;

public interface InfrastructureActionHandler<
    T extends NotificationCommand.NotificationInformation> {
  void publishCustomer(T NotifyData);
}
