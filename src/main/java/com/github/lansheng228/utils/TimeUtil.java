package com.github.lansheng228.utils;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtil {

  //睡眠 millis 毫秒
  public static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (Exception ignored) {
    }
  }
}
