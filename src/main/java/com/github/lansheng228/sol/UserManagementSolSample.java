package com.github.lansheng228.sol;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;

import com.github.lansheng228.Security;
import com.github.lansheng228.common.CommonConstant;
import com.github.lansheng228.utils.Environment;
import com.github.lansheng228.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;


@Slf4j
public class UserManagementSolSample {

  private static Web3j web3j;

  public static void main(String[] args) {
    web3j = Web3j.build(Environment.getService());
    // 获取URL
    URL url = Security.class.getClassLoader().getResource(CommonConstant.KEYSTORE_NAME);
    // 通过url获取File的绝对路径
    File f = new File(url.getFile());
    String password = "123";
    String keystorePath = f.getAbsolutePath();
    Credentials credentials = null;
    try {
      credentials = WalletUtils.loadCredentials(password, keystorePath);
    } catch (Exception e) {
      log.warn(e.getMessage());
    }

    String contractAddress = deploy(credentials);
    use(credentials, contractAddress);
  }

  private static String deploy(Credentials credentials) {
    ContractGasProvider contractGasProvider = new StaticGasProvider(
        Convert.toWei("10", Convert.Unit.GWEI).toBigInteger(),
        BigInteger.valueOf(3000000));

    RemoteCall<UserManagement> deploy  = UserManagement.deploy(web3j, credentials, contractGasProvider);

    String contractAddress = null;
    try {
      UserManagement userManagement = deploy.send();
      userManagement.isValid();
      contractAddress = userManagement.getContractAddress();
      log.info("Smart contract deployed to address: " + contractAddress);
    } catch (Exception e) {
      log.warn(e.getMessage());
    }

    return contractAddress;
  }

  private static void use(Credentials credentials, String contractAddress) {
    ContractGasProvider contractGasProvider = new StaticGasProvider(
        CommonConstant.GAS_PRICE,
        CommonConstant.GAS_LIMIT);

    UserManagement contract = UserManagement.load(contractAddress, web3j, credentials, contractGasProvider);
    String userAddress = "0xB0031507C4800AFFe12AAF070Da60C273b097a3A";
    String userName = "zhangsan";
    String userPassword = "123";
    String userNewPassword = "456";

    try {
      // 检查注册
      Boolean isRegister = contract.checkRegister(userName).send();
      log.info("用户名 {} 是否被注册：{}", userName, isRegister);
      if(!isRegister) {
        // 注册
        contract.register(userAddress, userName, userPassword).send();
        log.info("注册成功");
      }

      TimeUtil.sleep(10 * 1000);

      // 初次登录
      Boolean canLogin = contract.doLogin(userName, userPassword).send();
      if (canLogin) {
        log.info("初次登录: 登录成功");
      } else {
        log.info("初次登录: 登录失败");
      }

      //修改密码
      contract.updatePassword(userName, userNewPassword);
      log.info("修改密码成功");

      TimeUtil.sleep(10 * 1000);

      //使用旧密码再次登录
      canLogin = contract.doLogin(userName, userPassword).send();
      if (canLogin) {
        log.info("使用旧密码再次登录: 登录成功");
      } else {
        log.info("使用旧密码再次登录: 登录失败");
      }

      //使用新密码再次登录
      canLogin = contract.doLogin(userName, userNewPassword).send();
      if (canLogin) {
        log.info("使用新密码再次登录: 登录成功");
      } else {
        log.info("使用新密码再次登录: 登录失败");
      }

      // 获得用户信息
      Tuple2<String, String> userInfo = contract.getUserInfoByUserName(userName).send();
      log.info("{} 信息如下：{}  {}", userName, userInfo.component1(), userInfo.component2());

      // 系统用户人数
      BigInteger userNum = contract.getTotalUserNum().send();
      log.info("总共的用户数：" + userNum);
      // 输出所有用户信息
      for (BigInteger i = BigInteger.ZERO; i.compareTo(userNum) < 0; i = i.add(BigInteger.ONE)) {
        Tuple2<String, String> info = contract.getAllUserInfos(i).send();
        log.info("用户序号 {} 信息如下：{}  {}", i, info.component1(), info.component2());
      }

    } catch (Exception e) {
      log.warn(e.getMessage());
    }
  }
}


