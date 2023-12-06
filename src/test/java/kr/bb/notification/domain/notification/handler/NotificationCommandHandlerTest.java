package kr.bb.notification.domain.notification.handler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import bloomingblooms.domain.resale.ResaleNotificationData;
import bloomingblooms.domain.resale.ResaleNotificationList;
import java.util.ArrayList;
import java.util.List;
import kr.bb.notification.domain.notification.repository.MemberNotificationJpaRepository;
import kr.bb.notification.entity.MemberNotification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class NotificationCommandHandlerTest {
  @MockBean SimpleMessageListenerContainer simpleMessageListenerContainer;
  @Autowired NotificationCommandHandler notificationCommandHandler;

  @Autowired MemberNotificationJpaRepository memberNotificationJpaRepository;

  @Test
  @DisplayName("알림 저장 영속성 전이 handler")
  void save() {
    List<ResaleNotificationData> list = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      ResaleNotificationData name =
          ResaleNotificationData.builder().phoneNumber("010").userId(1L).userName("name").build();
      list.add(name);
    }

    ResaleNotificationList build =
        ResaleNotificationList.builder().resaleNotificationData(list).message("message").build();
    notificationCommandHandler.save(build);
    List<MemberNotification> all = memberNotificationJpaRepository.findAll();
    assertThat(all.size()).isEqualTo(5);
  }
}
