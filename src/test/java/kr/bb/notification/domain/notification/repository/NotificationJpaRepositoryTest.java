package kr.bb.notification.domain.notification.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import kr.bb.notification.domain.notification.entity.MemberNotification;
import kr.bb.notification.domain.notification.entity.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class NotificationJpaRepositoryTest {
  @Autowired private MemberNotificationJpaRepository memberNotificationJpaRepository;
  @Autowired private NotificationJpaRepository notificationJpaRepository;

  @Test
  @DisplayName("사용자 알림 리스트 조회")
  void findNotifications() {
    createNotifications();
    List<MemberNotification> notifications = memberNotificationJpaRepository.findNotifications(8L);
    List<Notification> all = notificationJpaRepository.findAll();
    for (MemberNotification m : notifications) {
      System.out.println(m.toString());
    }
    assertThat(notifications.size()).isEqualTo(8);
  }

  private void createNotifications() {
    Notification build =
        Notification.builder().notificationLink("link").notificationContent("content").build();

    for (int i = 0; i < 5; i++) {
      build
          .getMemberNotifications()
          .add(MemberNotification.builder().notification(build).userId(8L).isRead(true).build());
    }
    for (int i = 0; i < 3; i++) {
      build
          .getMemberNotifications()
          .add(MemberNotification.builder().notification(build).userId(8L).isRead(false).build());
    }
    notificationJpaRepository.save(build);
  }
}
