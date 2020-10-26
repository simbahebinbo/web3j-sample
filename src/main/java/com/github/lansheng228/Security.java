package com.github.lansheng228;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;

import com.github.lansheng228.common.CommonConstant;
import com.github.lansheng228.utils.Environment;
import lombok.extern.slf4j.Slf4j;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;

@Slf4j
public class Security {

  private static Web3j web3j;

  public static void main(String[] args) {
    web3j = Web3j.build(Environment.getService());

    // 获取URL
    URL url = Security.class.getClassLoader().getResource(CommonConstant.KEYSTORE_NAME);
    // 通过url获取File的绝对路径
    File f = new File(url.getFile());

    String password = "123";
    String directory = System.getProperty("user.home");
    String privateKey = exportPrivateKey(f.getAbsolutePath(), password);

    importPrivateKey(new BigInteger(privateKey, 16), password, directory);

    exportBip39Wallet(directory, password);
  }

  /**
   * 导出私钥
   *
   * @param keystorePath 账号的keystore路径
   * @param password 密码
   */
  private static String exportPrivateKey(String keystorePath, String password) {
    String privateKey = "";
    try {
      Credentials credentials = WalletUtils.loadCredentials(password, keystorePath);
      privateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);
      log.info(privateKey);
    } catch (Exception e) {
      log.warn(e.getMessage());
    }

    return privateKey;
  }

  /**
   * 导入私钥
   *
   * @param privateKey 私钥
   * @param password 密码
   * @param directory 存储路径 默认测试网络WalletUtils.getTestnetKeyDirectory() 默认主网络 WalletUtils.getMainnetKeyDirectory()
   */
  private static void importPrivateKey(BigInteger privateKey, String password, String directory) {
    ECKeyPair ecKeyPair = ECKeyPair.create(privateKey);
    try {
      String keystoreName = WalletUtils.generateWalletFile(password, ecKeyPair, new File(directory), true);
      log.info("keystore name " + keystoreName);
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
  }

  /**
   * 生成带助记词的账号
   */
  private static void exportBip39Wallet(String keystorePath, String password) {
    try {
      Bip39Wallet bip39Wallet = WalletUtils.generateBip39Wallet(password, new File(keystorePath));
      log.info(String.valueOf(bip39Wallet));
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
  }
}
