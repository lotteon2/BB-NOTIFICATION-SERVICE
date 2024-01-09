package kr.bb.notification.common.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class DuplicateEventHandlerAop {
  private final RedisTemplate<String, Object> redisTemplate;
  // https://alwayspr.tistory.com/34


  @Pointcut("@annotation(kr.bb.notification.common.annotation.DuplicateEventHandleAnnotation)")
  public void duplicateEvent() {}

  @Around("duplicateEvent()")
  public Object duplicateEventHandlerAop(ProceedingJoinPoint joinPoint) throws Throwable {
    String eventId = null;
    Object[] args = joinPoint.getArgs();

    // Check if data exists in Redis
    Object cachedData = redisTemplate.opsForValue().get(eventId);

    Object result = joinPoint.proceed(); // run method

    if (cachedData != null) {
      // Data exists in cache, return it without executing the method
      return cachedData;
    }

    // Data doesn't exist in cache, proceed to execute the method
    Object result = joinPoint.proceed();

    // Save the result in Redis with the specified TTL
    redisTemplate.opsForValue().set(key, result, duplicateEventHandlerAop.ttl());

    return result;
  }
}
