package kr.bb.notification.common.aop;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.notification.Role;
import bloomingblooms.domain.notification.stock.OutOfStockNotification;
import java.util.concurrent.TimeUnit;
import kr.bb.notification.common.annotation.OutOfStockNotificationDuplicateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class OutOfStockNotificationDuplicateAop {

  private final RedisTemplate<String, Object> redisTemplate;

  @Value("${cache.out-of-stock.expire}")
  private int expire;

  @Pointcut("@annotation(outOfStockNotificationDuplicateCheck)")
  public void outOfStockNotificationDuplicate(
      OutOfStockNotificationDuplicateCheck outOfStockNotificationDuplicateCheck) {}

  @Around(
      value =
          "outOfStockNotificationDuplicate(outOfStockNotificationDuplicateCheck) && args(outOfStockNotification)",
      argNames = "joinPoint,outOfStockNotification,outOfStockNotificationDuplicateCheck")
  public Object outOfStockNotificationDuplicateCheck(
      ProceedingJoinPoint joinPoint,
      NotificationData<OutOfStockNotification> outOfStockNotification,
      OutOfStockNotificationDuplicateCheck outOfStockNotificationDuplicateCheck)
      throws Throwable {
    Long storeId = outOfStockNotification.getWhoToNotify().getStoreId();
    Role role = outOfStockNotification.getPublishInformation().getRole();
    String eventType = outOfStockNotificationDuplicateCheck.getEventType();

    String generateEventId = storeId + ":" + role + ":" + eventType;
    Object event = redisTemplate.opsForValue().get(generateEventId);
    log.info("event id : " + generateEventId);

    if (event == null) {
      redisTemplate.opsForValue().set(generateEventId, eventType);
      redisTemplate.expire(generateEventId, expire, TimeUnit.MINUTES);

      Object[] args = joinPoint.getArgs();

      return joinPoint.proceed(args);
    }
    return null;
  }
}
