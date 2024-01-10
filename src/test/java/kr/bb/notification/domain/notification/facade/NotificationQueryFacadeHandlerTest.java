package kr.bb.notification.domain.notification.facade;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import bloomingblooms.domain.notification.Role;
import config.TestENV;
import java.util.ArrayList;
import java.util.List;
import kr.bb.notification.domain.notification.entity.MemberNotification;
import kr.bb.notification.domain.notification.entity.Notification;
import kr.bb.notification.domain.notification.entity.NotificationCommand.NotificationList;
import kr.bb.notification.domain.notification.entity.NotificationCommand.UnreadNotificationCount;
import kr.bb.notification.domain.notification.helper.NotificationQueryActionHelper;
import kr.bb.notification.domain.notification.repository.NotificationJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class NotificationQueryFacadeHandlerTest extends TestENV {

  @MockBean SimpleMessageListenerContainer simpleMessageListenerContainer;
  @Autowired NotificationJpaRepository notificationJpaRepository;
  @Autowired private NotificationQueryActionHelper notificationQueryFacadeHandler;

  private void createNotifications() {
    Notification build =
        Notification.builder().notificationLink("link").notificationContent("content").build();

    for (int i = 0; i < 5; i++) {
      build
          .getMemberNotifications()
          .add(
              MemberNotification.builder()
                  .role(Role.CUSTOMER)
                  .notification(build)
                  .userId(8L)
                  .isRead(true)
                  .build());
    }
    for (int i = 0; i < 3; i++) {
      build
          .getMemberNotifications()
          .add(
              MemberNotification.builder()
                  .role(Role.CUSTOMER)
                  .notification(build)
                  .userId(8L)
                  .isRead(false)
                  .build());
    }
    notificationJpaRepository.save(build);
  }

  @Test
  @DisplayName("알림 정보 조회")
  void getNotifications() {
    createNotifications();
    NotificationList notifications =
        notificationQueryFacadeHandler.getNotifications(8L, Role.CUSTOMER);
    assertThat(notifications.getNotifications().size()).isEqualTo(8);
    assertThat(notifications.getNotifications().get(0).getNotificationContent())
        .isEqualTo("content");
  }

  @Test
  @DisplayName("안읽은 알림 조회")
  void getUnreadNotification() {
    createNotification();
    UnreadNotificationCount unreadNotification =
        notificationQueryFacadeHandler.getUnreadNotification(10L, Role.CUSTOMER);
    assertThat(unreadNotification.getUnreadCount()).isEqualTo(3);
  }

  private void createNotification() {
    List<MemberNotification> list = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      list.add(MemberNotification.builder().role(Role.CUSTOMER).userId(10L).isRead(true).build());
    }
    for (int i = 0; i < 3; i++) {
      list.add(MemberNotification.builder().role(Role.CUSTOMER).userId(10L).isRead(false).build());
    }
    Notification build = Notification.builder().memberNotifications(list).build();
    notificationJpaRepository.save(build);
  }
}
