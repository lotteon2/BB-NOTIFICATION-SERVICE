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
    List<MemberNotification> notifications = memberNotificationJpaRepository.findNotifications(1L);
    List<Notification> all = notificationJpaRepository.findAll();
    assertThat(notifications.size()).isEqualTo(8);
    assertThat(all.size()).isEqualTo(1);
  }

  private void createNotifications() {
    List<MemberNotification> list = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      list.add(MemberNotification.builder().userId(1L).isRead(true).build());
    }
    for (int i = 0; i < 3; i++) {
      list.add(MemberNotification.builder().userId(1L).isRead(false).build());
    }
    Notification build =
        Notification.builder().notificationLink("link").memberNotifications(list).build();
    notificationJpaRepository.save(build);
  }
}
