package kr.bb.notification.domain.notification.entity;

import bloomingblooms.domain.resale.ResaleNotificationList;
import java.util.List;
import java.util.stream.Collectors;

public class MemberNotificationCommand {

  public static List<MemberNotification> toEntity(ResaleNotificationList whoToNotify) {
    return whoToNotify.getResaleNotificationData().stream()
        .map(item -> MemberNotification.builder().userId(item.getUserId()).build())
        .collect(Collectors.toList());
  }
}
