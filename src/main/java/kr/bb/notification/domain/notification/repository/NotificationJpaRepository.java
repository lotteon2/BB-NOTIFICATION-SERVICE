package kr.bb.notification.domain.notification.repository;


import kr.bb.notification.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {}
