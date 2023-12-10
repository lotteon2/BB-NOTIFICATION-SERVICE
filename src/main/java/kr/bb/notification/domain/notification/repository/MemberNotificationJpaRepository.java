package kr.bb.notification.domain.notification.repository;

import kr.bb.notification.domain.notification.entity.MemberNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberNotificationJpaRepository extends JpaRepository<MemberNotification, Long> {}
