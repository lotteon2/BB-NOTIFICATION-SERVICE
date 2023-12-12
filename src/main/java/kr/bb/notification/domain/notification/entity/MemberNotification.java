package kr.bb.notification.domain.notification.entity;

import bloomingblooms.domain.notification.Role;
import bloomingblooms.domain.resale.ResaleNotificationData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kr.bb.notification.common.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member_notification")
public class MemberNotification extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_notification_id")
  private Long memberNotificationId;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "role")
  private Role role;

  @Builder.Default
  @Column(name = "is_read")
  private Boolean isRead = false;

  @JsonIgnore
  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @JoinColumn(name = "notification_id")
  private Notification notification;

  public static List<MemberNotification> getMemberNotification(
      List<ResaleNotificationData> resaleNotificationData) {
    return resaleNotificationData.stream()
        .map(item -> MemberNotification.builder().userId(item.getUserId()).build())
        .collect(Collectors.toList());
  }
}
