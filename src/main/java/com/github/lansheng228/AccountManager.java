package com.github.lansheng228;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import com.github.lansheng228.utils.Environment;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;

/**
 * 账号管理相关
 */
@Slf4j
public class AccountManager {

  private static Admin admin;

  public static void main(String[] args) {
    admin = Admin.build(Environment.getService());
    String address = createNewAccount();
    getAccountList();
    unlockAccount(address);
  }

  /**
   * 创建账号
   */
  private static String createNewAccount() {
    String password = "123456789";
    String address = null;
    try {
      NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(password).send();
      address = newAccountIdentifier.getAccountId();
      log.info("new account address " + address);
    } catch (IOException e) {
      log.warn(e.getMessage());
    }

    return address;
  }

  /**
   * 获取账号列表
   */
  private static void getAccountList() {
    try {
      PersonalListAccounts personalListAccounts = admin.personalListAccounts().send();
      List<String> addressList;
      addressList = personalListAccounts.getAccountIds();
      log.info("account size " + addressList.size());
      for (String address : addressList) {
        log.info(address);
      }
    } catch (IOException e) {
      log.warn(e.getMessage());
    }
  }

  /**
   * 账号解锁
   */
  private static void unlockAccount(String address) {
    String password = "123456789";
    // 账号解锁持续时间 单位秒 缺省值300秒
    BigInteger unlockDuration = BigInteger.valueOf(60L);
    try {
      PersonalUnlockAccount personalUnlockAccount =
          admin.personalUnlockAccount(address, password, unlockDuration).send();
      Boolean isUnlocked = personalUnlockAccount.accountUnlocked();
      log.info("account unlock " + isUnlocked);
    } catch (IOException e) {
      log.warn(e.getMessage());
    }
  }
}
