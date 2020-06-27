package com.github.lansheng228;

import java.math.BigInteger;

import lombok.extern.slf4j.Slf4j;

import static java.lang.Integer.parseInt;

@Slf4j
public class IBAN {

  /**
   * 根据官方支持的IBAN规则生成二维码
   */
  public static void main(String[] args) {
    String address = "0xb4352408a1fAa75f49256D7E0665292d164F608c";
    String iban = getIBAN(address);
    log.info("地址  " + address + "  生成二维码 " + iban);
  }

  public static String getIBAN(String encodeAddress) {
    log.info("encodeAddress: " + encodeAddress);
    String address = encodeAddress.toLowerCase().substring(2);
    BigInteger value = new BigInteger(address, 16);
    StringBuilder bban = new StringBuilder(value.toString(36).toUpperCase());
    while (bban.length() < 15 * 2) {
      bban.insert(0, '0');
    }
    log.info("bban: " + bban);
    String iban = "XE00" + bban;

    iban = iban.substring(4) + iban.substring(0, 4);
    StringBuilder code = new StringBuilder();
    for (int i = 0; i < iban.length(); i++) {
      char chr = iban.charAt(i);
      int temp;
      if ((chr >= 'A') && (chr <= 'Z')) {
        temp = chr - 'A' + 10;
      } else {
        temp = chr - '0';
      }
      code.append(temp);
    }
    log.info("code: " + code);
    String remainder = code.toString();
    String block;
    while (remainder.length() > 2) {
      int endPoint = Math.min(remainder.length(), 9);
      block = remainder.substring(0, endPoint);
      remainder = parseInt(block, 10) % 97 + remainder.substring(block.length());
    }
    log.info("remainder: " + remainder);

    int checkNum = parseInt(remainder, 10) % 97;
    String checkDigit = ("0" + (98 - checkNum));
    checkDigit = checkDigit.substring(checkDigit.length() - 2);
    log.info("checkDigit: " + checkDigit);
    String IBAN = "XE" + checkDigit + bban;
    String qrCodeString = "iban:" + IBAN + "?token=ETH&amount=5";
    log.info("IBAN " + IBAN);
    log.info("验证 " + validateIBAN(IBAN));
    log.info("qrcode " + qrCodeString);
    String decodeAddress = decodeQRString(qrCodeString);

    if (encodeAddress.equalsIgnoreCase(decodeAddress)) {
      return IBAN;
    } else {
      log.warn("生成的二维码无法解码");
      return null;
    }
  }

  private static boolean validateIBAN(String iban) {
    int len = iban.length();
    if ((len < 4) || (!iban.matches("[0-9A-Z]+"))) {
      return false;
    }

    iban = iban.substring(4) + iban.substring(0, 4);

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < len; i++) {
      sb.append(Character.digit(iban.charAt(i), 36));
    }

    BigInteger bigInt = new BigInteger(sb.toString());
    int mod = bigInt.mod(BigInteger.valueOf(97)).intValue();
    log.info("mod: " + mod);

    return mod == 1;
  }

  private static String decodeQRString(String result) {
    int ibanEndpoint = result.indexOf("?");
    String iban = result.substring(5, ibanEndpoint < 0 ? result.length() : ibanEndpoint);
    String address = IBAN2Address(iban);
    String query = result.substring(ibanEndpoint + 1);
    String[] params = query.split("&");
    String token = null;
    String amount = null;
    for (String param : params) {
      if (param.startsWith("token=")) {
        token = param.substring(6);
        continue;
      }
      if (param.startsWith("amount=")) {
        amount = param.substring(7);
      }
    }
    log.info("decodeQRString");
    log.info("address " + address);
    log.info("token " + token);
    log.info("amount " + amount);

    return address;
  }

  private static String IBAN2Address(String iban) {
    String base36 = iban.substring(4);
    StringBuilder base16 = new StringBuilder(new BigInteger(base36, 36).toString(16));
    while (base16.length() < 40) {
      base16.insert(0, "0");
    }
    return "0x" + base16.toString().toLowerCase();
  }
}
