package com.github.lansheng228;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClearMemory {

  public static Unsafe UNSAFE = null;

  public static void main(String[] args) {
    String password = new String("11111111");
    String fake = new String(password.replaceAll(".", "?"));
    log.info(password); // l00k@myHor$e
    log.info(fake); // ????????????

    getUnsafe().copyMemory(fake, 0L, null, toAddress(password), sizeOf(password));
    //		getUnsafe().copyMemory(fake, 0L, null, toAddress(password), password.getBytes().length);

    log.info(password); // ????????????
    log.info(fake); // ????????????
  }

  public static Unsafe getUnsafe() {
    if (UNSAFE != null) return UNSAFE;
    try {
      Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");

      theUnsafe.setAccessible(true);
      UNSAFE = (Unsafe) theUnsafe.get(null);
      log.info(String.valueOf(UNSAFE));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      log.warn(e.getMessage());
    }
    return UNSAFE;
  }

  public static long toAddress(Object obj) {
    Object[] array = new Object[] {obj};
    long baseOffset = getUnsafe().arrayBaseOffset(Object[].class);
    return normalize(getUnsafe().getInt(array, baseOffset));
  }

  public static Object fromAddress(long address) {
    Object[] array = new Object[] {null};
    long baseOffset = getUnsafe().arrayBaseOffset(Object[].class);
    getUnsafe().putLong(array, baseOffset, address);
    return array[0];
  }

  public static long normalize(int value) {
    if (value >= 0) return value;
    return (~0L >>> 32) & value;
  }

  public static long sizeOf(Object o) {
    Unsafe u = getUnsafe();
    HashSet<Field> fields = new HashSet<Field>();
    Class c = o.getClass();
    while (c != Object.class) {
      for (Field f : c.getDeclaredFields()) {
        if ((f.getModifiers() & Modifier.STATIC) == 0) {
          fields.add(f);
        }
      }
      c = c.getSuperclass();
    }

    // 获得偏移
    long maxSize = 0;
    for (Field f : fields) {
      long offset = u.objectFieldOffset(f);
      if (offset > maxSize) {
        maxSize = offset;
      }
    }

    return ((maxSize / 8) + 1) * 8; // padding
  }
}
