package kr.bb.notification.common.aop;

import bloomingblooms.domain.notification.Role;
import java.util.concurrent.TimeUnit;
import kr.bb.notification.common.annotation.DuplicateEventHandleAnnotation;
import kr.bb.notification.domain.notification.entity.NotificationCommand.NotificationInformation;
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
public class DuplicateEventHandlerAop {
  private final RedisTemplate<String, Object> redisTemplate;

  @Value("${cache.expire}")
  private int expire;

  @Pointcut("@annotation(duplicateEventHandleAnnotation)")
  public void duplicateEvent(DuplicateEventHandleAnnotation duplicateEventHandleAnnotation) {}

  @Around(
      value = "duplicateEvent(duplicateEventHandleAnnotation) && args(notifyData)",
      argNames = "joinPoint,notifyData,duplicateEventHandleAnnotation")
  public Object duplicateEventHandlerAop(
      ProceedingJoinPoint joinPoint,
      NotificationInformation notifyData,
      DuplicateEventHandleAnnotation duplicateEventHandleAnnotation)
      throws Throwable {
    String eventId = notifyData.getEventId();
    String id = String.valueOf(notifyData.getId());
    Role role = notifyData.getRole();
    String eventType = duplicateEventHandleAnnotation.getEventType();

    String generateEventId = eventId + ":" + id + ":" + role + ":" + eventType;
    Object event = redisTemplate.opsForValue().get(generateEventId);
    log.info("event id : " + generateEventId);

    if (event == null) {
      redisTemplate.opsForValue().set(generateEventId, eventType);
      redisTemplate.expire(generateEventId, expire, TimeUnit.MINUTES);

      Object[] args = joinPoint.getArgs();

      return joinPoint.proceed(args);
    } else {
      return null;
    }
  }
}
