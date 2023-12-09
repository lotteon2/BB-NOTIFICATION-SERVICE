package kr.bb.notification.domain.notification.repository;

import java.util.List;
import kr.bb.notification.domain.notification.entity.MemberNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberNotificationJpaRepository extends JpaRepository<MemberNotification, Long> {
  @Query(
      "SELECT m from MemberNotification m "
          + "left join Notification n "
          + "on m.notification.notificationId=n.notificationId "
          + "where m.userId=:userId")
  List<MemberNotification> findNotifications(Long userId);
}
