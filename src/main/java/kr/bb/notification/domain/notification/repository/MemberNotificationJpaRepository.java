package kr.bb.notification.domain.notification.repository;

import kr.bb.notification.domain.notification.entity.MemberNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberNotificationJpaRepository extends JpaRepository<MemberNotification, Long> {
  @Query("select count(m) from MemberNotification m where m.userId=:userId and m.isRead=false")
  Long findUnreadNotificationCount(Long userId);
}
