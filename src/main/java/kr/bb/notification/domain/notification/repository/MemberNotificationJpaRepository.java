package kr.bb.notification.domain.notification.repository;

import bloomingblooms.domain.notification.Role;
import java.util.List;
import kr.bb.notification.domain.notification.entity.MemberNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MemberNotificationJpaRepository extends JpaRepository<MemberNotification, Long> {
  @Query(
      "SELECT m from MemberNotification m "
          + "left join Notification n "
          + "on m.notification.notificationId=n.notificationId "
          + "where m.userId=:userId and m.role=:role "
          + "order by m.createdAt desc ")
  List<MemberNotification> findNotifications(Long userId, Role role);

  @Query(
      "select count(m) from MemberNotification m where m.userId=:userId and m.isRead=false and m.role=:role")
  Long findUnreadNotificationCount(Long userId, Role role);

  @Modifying
  @Transactional
  @Query(
      "update MemberNotification m set m.isRead=true where m.notification.notificationId in :notificationId and m.userId=:userId and m.role=:role")
  void updateNotificationIsRead(
      @Param("notificationId") List<Long> notificationId,
      @Param("userId") Long userId,
      @Param("role") Role role);
}
