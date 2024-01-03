package kr.bb.notification.domain.notification.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import bloomingblooms.domain.notification.Role;
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
    List<MemberNotification> notifications =
        memberNotificationJpaRepository.findNotifications(8L, Role.CUSTOMER);
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
          .add(
              MemberNotification.builder()
                  .notification(build)
                  .userId(8L)
                  .role(Role.CUSTOMER)
                  .isRead(true)
                  .build());
    }
    for (int i = 0; i < 3; i++) {
      build
          .getMemberNotifications()
          .add(
              MemberNotification.builder()
                  .notification(build)
                  .userId(8L)
                  .role(Role.CUSTOMER)
                  .isRead(false)
                  .build());
    }
    notificationJpaRepository.save(build);
  }

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
