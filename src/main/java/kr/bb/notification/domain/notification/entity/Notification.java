package kr.bb.notification.domain.notification.entity;


import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(name = "notification")
public class Notification {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "notification_id")
  private Long notificationId;

  @Column(name = "notification_content")
  private String notificationContent;

  @Column(name = "notification_link")
  private String notificationLink;

  @OneToMany(
      mappedBy = "notification",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      orphanRemoval = true)
  @Builder.Default
  private List<MemberNotification> memberNotifications = new ArrayList<>();

  public void setMemberNotifications(List<MemberNotification> memberNotifications) {
    this.memberNotifications = memberNotifications;
  }
}
