package config;

import kr.bb.notification.common.annotation.DuplicateEventHandleAnnotation;
import kr.bb.notification.common.aop.DuplicateEventHandlerAop;
import org.redisson.api.RedissonClient;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class TestENV {
  @MockBean private RedissonClient redissonClient;
  @MockBean private DuplicateEventHandlerAop duplicateEventHandlerAop;
  @MockBean private MetricsAutoConfiguration metricsAutoConfiguration;
}
