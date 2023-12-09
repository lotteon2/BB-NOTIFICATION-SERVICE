package kr.bb.notification.domain.notification.facade;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import kr.bb.notification.domain.notification.entity.MemberNotification;
import kr.bb.notification.domain.notification.entity.Notification;
import kr.bb.notification.domain.notification.entity.NotificationCommand.NotificationList;
import kr.bb.notification.domain.notification.repository.NotificationJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class NotificationQueryFacadeHandlerTest {
  @Autowired private NotificationQueryFacadeHandler notificationQueryFacadeHandler;
  @Autowired private NotificationJpaRepository notificationJpaRepository;

  private void createNotifications() {
    Notification build =
        Notification.builder().notificationLink("link").notificationContent("content").build();

    for (int i = 0; i < 5; i++) {
      build
          .getMemberNotifications()
          .add(MemberNotification.builder().notification(build).userId(1L).isRead(true).build());
    }
    for (int i = 0; i < 3; i++) {
      build
          .getMemberNotifications()
          .add(MemberNotification.builder().notification(build).userId(1L).isRead(false).build());
    }
    notificationJpaRepository.save(build);
  }

  @Test
  @DisplayName("알림 정보 조회")
  void getNotifications() {
    createNotifications();
    NotificationList notifications = notificationQueryFacadeHandler.getNotifications(1L);
    assertThat(notifications.getNotifications().size()).isEqualTo(8);
    assertThat(notifications.getNotifications().get(0).getNotificationContent())
        .isEqualTo("content");
  }
}
