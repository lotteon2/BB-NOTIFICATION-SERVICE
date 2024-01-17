package kr.bb.notification.domain.notification.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import bloomingblooms.domain.notification.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import kr.bb.notification.domain.notification.entity.MemberNotification;
import kr.bb.notification.domain.notification.entity.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberNotificationJpaRepositoryTest {
  @Autowired MemberNotificationJpaRepository memberNotificationJpaRepository;
  @Autowired NotificationJpaRepository notificationJpaRepository;
  @Autowired EntityManager em;

  @Test
  @DisplayName("안읽은 알림 개수 조회")
  void findUnreadNotificationCount() {
    List<MemberNotification> list = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      list.add(MemberNotification.builder().role(Role.CUSTOMER).userId(10L).isRead(true).build());
    }
    for (int i = 0; i < 3; i++) {
      list.add(MemberNotification.builder().role(Role.CUSTOMER).userId(10L).isRead(false).build());
    }
    Notification build = Notification.builder().memberNotifications(list).build();
    notificationJpaRepository.save(build);

    Long unreadNotificationCount =
        memberNotificationJpaRepository.findUnreadNotificationCount(10L, Role.CUSTOMER);
    assertThat(unreadNotificationCount).isEqualTo(3);
  }

  @Test
  @DisplayName("알림 읽음 처리")
  void updateNotificationIsRead() {
    for (int i = 0; i < 5; i++) {
      Notification notification =
          Notification.builder()
              .notificationContent("content" + i)
              .notificationLink("link" + i)
              .build();
      MemberNotification memberNotification =
          MemberNotification.builder()
              .notification(notification)
              .role(Role.CUSTOMER)
              .userId(1L)
              .build();
      notification.getMemberNotifications().add(memberNotification);
      notificationJpaRepository.save(notification);
    }
    List<Notification> all = notificationJpaRepository.findAll();
    memberNotificationJpaRepository.updateNotificationIsRead(
        List.of(all.get(0).getNotificationId(), all.get(1).getNotificationId()), 1L, Role.CUSTOMER);
    em.flush();
    em.clear();
    List<MemberNotification> notifications =
        memberNotificationJpaRepository.findNotifications(1L, Role.CUSTOMER);

    List<MemberNotification> collect =
        notifications.stream().filter(MemberNotification::getIsRead).collect(Collectors.toList());
    assertThat(collect.size()).isEqualTo(2);
  }
}
