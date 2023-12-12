package kr.bb.notification.domain.notification.infrastructure.dto;

import com.amazonaws.services.ec2.model.StartInstancesRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryNotification {
  private Long userId;
  private String phoneNumber;
}
