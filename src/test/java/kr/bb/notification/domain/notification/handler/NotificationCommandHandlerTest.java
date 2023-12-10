package kr.bb.notification.domain.notification.handler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import bloomingblooms.domain.resale.ResaleNotificationData;
import bloomingblooms.domain.resale.ResaleNotificationList;
import java.util.ArrayList;
import java.util.List;
import kr.bb.notification.domain.notification.application.NotificationCommandService;
import kr.bb.notification.domain.notification.repository.MemberNotificationJpaRepository;
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
  @Autowired
  NotificationCommandService notificationCommandHandler;

  @Autowired MemberNotificationJpaRepository memberNotificationJpaRepository;

}
