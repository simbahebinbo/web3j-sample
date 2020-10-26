package com.github.lansheng228.common;


import java.math.BigInteger;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonConstant {

  /**
   * 分隔符
   */
  public static final String SEPARATOR_SEMICOLON = ";";

  public static final String SEPARATOR_QUESTION_MARK = "?";

  public static final String SEPARATOR_COLON = ":";

  public static final String SEPARATOR_COLON2 = "::";

  public static final String SEPARATOR_COMMA = ",";

  public static final String SEPARATOR_AMPERSAND = "&";

  public static final String SEPARATOR_EQUAL_SIGN = "=";

  /**
   * 斜线
   */
  public static final String SEPARATOR_VIRGULE = "/";

  public static final String SEPARATOR_BLANK = "";

  public static final String SEPARATOR_ASTERISK = "*";

  /**
   * 竖线
   */
  public static final String SEPARATOR_VERTICAL_LINE = "|";

  /**
   * 下划线
   */
  public static final String SEPARATOR_UNDERSCORE = "_";
  /**
   * 连字符 中横线
   */
  public static final String SEPARATOR_HYPHEN = "-";

  // GAS价格
  public static final BigInteger GAS_PRICE = BigInteger.valueOf(2000_000_000L);

  // GAS上限
  public static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000L);

  // 交易费用
  public static final BigInteger GAS_VALUE = BigInteger.valueOf(100_000L);
}
