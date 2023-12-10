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
  @Autowired NotificationJpaRepository notificationJpaRepository;
  @Autowired MemberNotificationJpaRepository memberNotificationJpaRepository;

  @Test
  @DisplayName("알림 저장 테스트 ")
  void saveNotification() {
    Notification notiContent =
        Notification.builder()
            .notificationContent("noti content")
            .notificationLink("/api/products/1")
            .build();
    MemberNotification build =
        MemberNotification.builder().notification(notiContent).userId(1L).build();
    memberNotificationJpaRepository.save(build);
    List<Notification> all = notificationJpaRepository.findAll();
    assertThat(all.size()).isNotZero();
  }

  @Test
  @DisplayName("알림 저장 테스트 영속성 전이")
  void saveNotificationAndMemberNotification() {
    List<MemberNotification> list = new ArrayList<>();
    Notification content =
        Notification.builder()
            .notificationContent("content")
            .notificationLink("/api/products/1")
            .build();
    for (int i = 0; i < 5; i++) {
      MemberNotification build =
          MemberNotification.builder().userId(1L).notification(content).build();
      list.add(build);
    }
    content.setMemberNotifications(list);
    notificationJpaRepository.save(content);
    List<MemberNotification> all = memberNotificationJpaRepository.findAll();
    assertThat(all.size()).isGreaterThan(4);
  }
}
