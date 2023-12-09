package kr.bb.notification.domain.notification.facade;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import kr.bb.notification.domain.notification.entity.MemberNotification;
import kr.bb.notification.domain.notification.entity.Notification;
import kr.bb.notification.domain.notification.entity.NotificationCommand.UnreadNotificationCount;
import kr.bb.notification.domain.notification.repository.NotificationJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class NotificationQueryFacadeHandlerTest {
  @Autowired NotificationJpaRepository notificationJpaRepository;
  @Autowired private NotificationQueryFacadeHandler notificationQueryFacadeHandler;

  @Test
  @DisplayName("안읽은 알림 조회")
  void getUnreadNotification() {
    createNotification();
    UnreadNotificationCount unreadNotification =
        notificationQueryFacadeHandler.getUnreadNotification(1L);
    assertThat(unreadNotification.getUnreadCount()).isEqualTo(3);
  }

  private void createNotification() {
    List<MemberNotification> list = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      list.add(MemberNotification.builder().userId(1L).isRead(true).build());
    }
    for (int i = 0; i < 3; i++) {
      list.add(MemberNotification.builder().userId(1L).isRead(false).build());
    }
    Notification build = Notification.builder().memberNotifications(list).build();
    notificationJpaRepository.save(build);
  }
}
