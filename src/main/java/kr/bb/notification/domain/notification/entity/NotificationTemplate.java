package kr.bb.notification.domain.notification.entity;


public class NotificationTemplate {
  public static String setContent(String type, String body, String redirectUrl) {
    return type + "\n" + body + "\n" + redirectUrl;
  }
}
