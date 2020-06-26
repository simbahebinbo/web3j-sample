package com.github.lansheng228;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.github.lansheng228.utils.Environment;
import com.github.lansheng228.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.geth.Geth;
import org.web3j.utils.Convert;

@Slf4j
public class TransactionClient {

  private static Web3j web3j;
  private static Geth geth;


  private static BigDecimal defaultGasPrice = BigDecimal.valueOf(5);
  private static String fromAddress = "0xb4352408a1fAa75f49256D7E0665292d164F608c";
  private static String toAddress = "0xB0031507C4800AFFe12AAF070Da60C273b097a3A";

  public static void main(String[] args) {
    web3j = Web3j.build(Environment.getService());
    geth = Geth.build(Environment.getService());

    log.info("转账前:");
    getBalance(fromAddress);
    sendTransaction();
    TimeUtil.sleep(10 * 1000);
    log.info("转账后:");
    getBalance(fromAddress);
  }

  /**
   * 获取余额
   *
   * @param address 钱包地址
   * @return 余额
   */
  private static BigInteger getBalance(String address) {
    BigInteger balance = null;
    try {
      EthGetBalance ethGetBalance =
          web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
      balance = ethGetBalance.getBalance();
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
    log.info("address " + address + " balance " + balance + "wei");
    return balance;
  }

  /**
   * 生成一个普通交易对象
   *
   * @param fromAddress 放款方
   * @param toAddress 收款方
   * @param nonce 交易序号
   * @param gasPrice gas 价格
   * @param gasLimit gas 数量
   * @param value 金额
   * @return 交易对象
   */
  private static Transaction makeTransaction(
      String fromAddress,
      String toAddress,
      BigInteger nonce,
      BigInteger gasPrice,
      BigInteger gasLimit,
      BigInteger value) {
    Transaction transaction;
    transaction =
        Transaction.createEtherTransaction(
            fromAddress, nonce, gasPrice, gasLimit, toAddress, value);
    return transaction;
  }

  /**
   * 获取普通交易的gas上限
   *
   * @param transaction 交易对象
   * @return gas 上限
   */
  private static BigInteger getTransactionGasLimit(Transaction transaction) {
    BigInteger gasLimit = BigInteger.ZERO;
    try {
      EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
      gasLimit = ethEstimateGas.getAmountUsed();
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
    return gasLimit;
  }

  /**
   * 获取账号交易次数 nonce
   *
   * @param address 钱包地址
   * @return nonce
   */
  private static BigInteger getTransactionNonce(String address) {
    BigInteger nonce = BigInteger.ZERO;
    try {
      EthGetTransactionCount ethGetTransactionCount =
          web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
      nonce = ethGetTransactionCount.getTransactionCount();
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
    return nonce;
  }

  /**
   * 发送一个普通交易
   *
   * @return 交易 Hash
   */
  private static String sendTransaction() {
    String password = "123";
    //解锁有效时间，单位秒
    BigInteger unlockDuration = BigInteger.valueOf(60L);
    BigDecimal amount = new BigDecimal("0.00000555");
    String txHash = null;

    try {
      PersonalUnlockAccount personalUnlockAccount =
          geth.personalUnlockAccount(fromAddress, password, unlockDuration).send();
      if (personalUnlockAccount.accountUnlocked()) {
        log.info("解锁成功");
        BigInteger value = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
        log.info("转账金额:" + value);
        Transaction transaction = makeTransaction(fromAddress, toAddress, null, null, null, value);
        // 不是必须的 可以使用默认值
        BigInteger gasLimit = getTransactionGasLimit(transaction);
        log.info("gasLimit:" + gasLimit);
        // 不是必须的 缺省值就是正确的值
        BigInteger nonce = getTransactionNonce(fromAddress);
        log.info("nonce:" + nonce);
        // 该值为大部分矿工可接受的gasPrice
        BigInteger gasPrice = Convert.toWei(defaultGasPrice, Convert.Unit.GWEI).toBigInteger();
        log.info("gasPrice:" + gasPrice);
        transaction = makeTransaction(fromAddress, toAddress, nonce, gasPrice, gasLimit, value);
        EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).send();
        txHash = ethSendTransaction.getTransactionHash();
      }
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
    log.info("tx hash " + txHash);
    return txHash;
  }
}
