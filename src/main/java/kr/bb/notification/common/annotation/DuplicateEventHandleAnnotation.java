package kr.bb.notification.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicateEventHandleAnnotation {
  String eventId(); // Specify eventId for constructing the cache key

  String userId(); // Specify userId for constructing the cache key

  long ttl() default 180; // Default TTL is 3 minutes
}
