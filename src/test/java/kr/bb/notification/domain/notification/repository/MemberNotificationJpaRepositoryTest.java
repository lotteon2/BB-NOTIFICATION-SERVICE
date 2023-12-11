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
class MemberNotificationJpaRepositoryTest {
  @Autowired MemberNotificationJpaRepository memberNotificationJpaRepository;
  @Autowired NotificationJpaRepository notificationJpaRepository;

  @Test
  @DisplayName("안읽은 알림 개수 조회")
  void findUnreadNotificationCount() {
    List<MemberNotification> list = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      list.add(MemberNotification.builder().userId(10L).isRead(true).build());
    }
    for (int i = 0; i < 3; i++) {
      list.add(MemberNotification.builder().userId(10L).isRead(false).build());
    }
    Notification build = Notification.builder().memberNotifications(list).build();
    notificationJpaRepository.save(build);

    Long unreadNotificationCount = memberNotificationJpaRepository.findUnreadNotificationCount(10L);
    assertThat(unreadNotificationCount).isEqualTo(3);
  }
}
