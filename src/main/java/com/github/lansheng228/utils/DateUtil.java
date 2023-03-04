package com.github.lansheng228.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Clock;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    private static final Clock clock = Clock.systemDefaultZone();

    public static long currentTimeMillis() {
        return clock.millis();
    }
}
