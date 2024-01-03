package kr.bb.notification.domain.notification.entity;

import bloomingblooms.domain.notification.Role;
import bloomingblooms.domain.resale.ResaleNotificationList;
import java.util.List;
import java.util.stream.Collectors;

public class MemberNotificationCommand {

  public static List<MemberNotification> toEntityList(
      ResaleNotificationList whoToNotify, Notification notification) {
    return whoToNotify.getResaleNotificationData().stream()
        .map(
            item ->
                MemberNotification.builder()
                    .notification(notification)
                    .userId(item.getUserId())
                    .role(Role.CUSTOMER)
                    .build())
        .collect(Collectors.toList());
  }

  public static MemberNotification toEntity(Long id, Role role, Notification notification) {
    return MemberNotification.builder().userId(id).role(role).notification(notification).build();
  }
}
