package com.github.lansheng228.common;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

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

    // 节点地址
    public static String NODE_URL = "http://172.18.18.141:8545";

    //节点用户名 和 密码
    public static String NODE_USER = "";

    public static String NODE_PASSWORD = "";

    // 账户密码
    public static String ACCOUNT_PASSWORD = "123";

    // 账户助记词
    public static String ACCOUNT_MNEMONIC = "forget list latin genuine ordinary mad reflect bridge hedgehog vocal waste type";

    // 账户文件路径
    public static String ACCOUNT_KEYSTORE_NAME = "UTC--2020-06-26T04-45-06.821393158Z--c719405d30703230359afe351f32e364ab26e8ee";

    // chain id, 在创世区块中定义的
    public static long CHAIN_ID = 12121212;
}
