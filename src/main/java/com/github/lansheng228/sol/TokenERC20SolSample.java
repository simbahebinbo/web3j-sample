package com.github.lansheng228.sol;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;

import com.github.lansheng228.Security;
import com.github.lansheng228.common.CommonConstant;
import com.github.lansheng228.utils.Environment;
import lombok.extern.slf4j.Slf4j;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;


@Slf4j
public class TokenERC20SolSample {

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

    RemoteCall<TokenERC20> deploy = TokenERC20.deploy(web3j, credentials,
        contractGasProvider,
        BigInteger.valueOf(5201314),
        "my token", "mt");
    String contractAddress = null;
    try {
      TokenERC20 tokenERC20 = deploy.send();
      tokenERC20.isValid();
      contractAddress = tokenERC20.getContractAddress();
      log.info("Smart contract deployed to address: " + contractAddress);
    } catch (Exception e) {
      log.warn(e.getMessage());
    }

    return contractAddress;
  }

  private static void use(Credentials credentials, String contractAddress) {
    ContractGasProvider contractGasProvider = new StaticGasProvider(
        Convert.toWei("10", Convert.Unit.GWEI).toBigInteger(),
        BigInteger.valueOf(100000));

    TokenERC20 contract = TokenERC20.load(contractAddress, web3j, credentials, contractGasProvider);
    String myAddress = "0xB0031507C4800AFFe12AAF070Da60C273b097a3A";

    try {
      //从发行账户向某个地址转账
      contract.transfer(myAddress, BigInteger.valueOf(1000000000000000000L)).send();
      //该账户余额
      log.info("myAddress {} balance(wei): {}", myAddress, contract.balanceOf(myAddress).send());
      //发行的币总量
      log.info("totalSupply(wei): " + contract.totalSupply().send());
      //币符号
      log.info("symbol: " + contract.symbol().send());
      //币名
      log.info("name: " + contract.name().send());
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
  }
}


