package kr.bb.notification.common.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResaleNotifiatoiinDtoList {
  private List<ResaleNotifiattionData> resaleNotifiattionData;
  private String notificationMessage;
}
