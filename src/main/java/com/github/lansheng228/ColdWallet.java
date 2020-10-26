package com.github.lansheng228;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lansheng228.common.CommonConstant;
import com.github.lansheng228.utils.Environment;
import com.github.lansheng228.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.ChainIdLong;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

/** 冷钱包 账号 交易相关 */
@Slf4j
public class ColdWallet {

  private static Web3j web3j;
  private static Admin admin;

  private static final BigDecimal defaultGasPrice = BigDecimal.valueOf(5);
  private static final String fromAddr = "0xB0031507C4800AFFe12AAF070Da60C273b097a3A";
  private static final String fromPassword = "123";
  private static final String toAddr = "0xb4352408a1fAa75f49256D7E0665292d164F608c";
  private static final String contractAddress = "0x284c95974c381b7deee40e5efbd89af00e6bcfea";
  private static final String walletPassword = "11111111";

  public static void main(String[] args) {
    web3j = Web3j.build(Environment.getService());
    admin = Admin.build(Environment.getService());

    try {
      //创建冷钱包 得到地址和私钥
      WalletFile walletFile = createWallet(walletPassword);
      String walletAddress = new Address(walletFile.getAddress()).toString();
      log.info("wallet address: " + walletAddress);

      ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
      String keystore = objectMapper.writeValueAsString(walletFile);
      String privateKey = decryptWallet(keystore, walletPassword);
      log.info("wallet privateKey: " + privateKey);

      log.info("转入钱包之前： walletAddress: " + walletAddress + " ethBalance(wei): " + getETHBalance(walletAddress)  + "    tokenBalance: " + getTokenBalance(walletAddress, contractAddress));
      log.info("转入钱包之前： fromAddress: " + fromAddr + " ethBalance(wei): " + getETHBalance(fromAddr)  + "    tokenBalance: " + getTokenBalance(fromAddr, contractAddress));
      log.info("转入钱包之前： toAddress: " + toAddr + " ethBalance(wei): " + getETHBalance(toAddr)  + "    tokenBalance: " + getTokenBalance(toAddr, contractAddress));

      //给新创建的冷钱包转 ETH 和 token
      sendTransaction(fromAddr, fromPassword, walletAddress, BigDecimal.valueOf(10.123456));
      sendTokenTransaction(fromAddr, fromPassword, contractAddress, walletAddress, BigInteger.valueOf(1234564321L));

      TimeUtil.sleep(10 * 1000);
      log.info("转入钱包之后： walletAddress: " + walletAddress + " ethBalance(wei): " + getETHBalance(walletAddress) + "   tokenBalance: " + getTokenBalance(walletAddress, contractAddress));
      log.info("转入钱包之后： fromAddress: " + fromAddr + " ethBalance(wei): " + getETHBalance(fromAddr)  + "    tokenBalance: " + getTokenBalance(fromAddr, contractAddress));
      log.info("转入钱包之后： toAddress: " + toAddr + " ethBalance(wei): " + getETHBalance(toAddr)  + "    tokenBalance: " + getTokenBalance(toAddr, contractAddress));

      //从冷钱包转账到某个地址
      testTransaction(walletAddress, privateKey, toAddr, BigDecimal.valueOf(0.512789));
      testTokenTransaction(walletAddress, privateKey, contractAddress, toAddr, BigInteger.valueOf(12398L));

      TimeUtil.sleep(10 * 1000);
      log.info("转出钱包之后： walletAddress: " + walletAddress + " ethBalance(wei): " + getETHBalance(walletAddress) + "  tokenBalance: " + getTokenBalance(walletAddress, contractAddress));
      log.info("转出钱包之后： fromAddress: " + fromAddr + " ethBalance(wei): " + getETHBalance(fromAddr)  + "    tokenBalance: " + getTokenBalance(fromAddr, contractAddress));
      log.info("转出钱包之后： toAddress: " + toAddr + " ethBalance(wei): " + getETHBalance(toAddr)  + "    tokenBalance: " + getTokenBalance(toAddr, contractAddress));
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
  }

  private static void testTransaction(String from, String privateKey, String to, BigDecimal amount) {
    BigInteger nonce;
    EthGetTransactionCount ethGetTransactionCount = null;
    try {
      ethGetTransactionCount =
          web3j.ethGetTransactionCount(from, DefaultBlockParameterName.PENDING).send();
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
    if (ethGetTransactionCount == null) {
      return;
    }
    nonce = ethGetTransactionCount.getTransactionCount();
    log.info("address: {} nonce: {} ", from, nonce);
    BigInteger gasPrice = CommonConstant.GAS_PRICE;
    BigInteger gasLimit = CommonConstant.GAS_LIMIT;
    BigInteger value = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
    String data = "";
    long chainId = CommonConstant.CHAIN_ID;
    String signedData;
    try {
      signedData = signTransaction(nonce, gasPrice, gasLimit, to, value, data, chainId, privateKey);
      EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedData).send();
      log.info("交易哈希: "+ ethSendTransaction.getTransactionHash());
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
  }

  private static void testTokenTransaction(
      String from,
      String privateKey,
      String contract,
      String to,
      BigInteger amount) {
    BigInteger nonce;
    EthGetTransactionCount ethGetTransactionCount = null;
    try {
      ethGetTransactionCount =
          web3j.ethGetTransactionCount(from, DefaultBlockParameterName.PENDING).send();
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
    if (ethGetTransactionCount == null) {
      return;
    }
    nonce = ethGetTransactionCount.getTransactionCount();
    log.info("nonce " + nonce);
    BigInteger gasPrice = CommonConstant.GAS_PRICE;
    BigInteger gasLimit = CommonConstant.GAS_LIMIT;
    BigInteger value = BigInteger.ZERO;
    // token转账参数
    String methodName = "transfer";
    List<Type> inputParameters = new ArrayList<>();
    List<TypeReference<?>> outputParameters = new ArrayList<>();
    Address tAddress = new Address(to);
    Uint256 tokenValue = new Uint256(amount);
    inputParameters.add(tAddress);
    inputParameters.add(tokenValue);

    TypeReference<Bool> typeReference = new TypeReference<Bool>() {};
    outputParameters.add(typeReference);

    Function function = new Function(methodName, inputParameters, outputParameters);
    String data = FunctionEncoder.encode(function);

    long chainId = CommonConstant.CHAIN_ID;
    String signedData;
    try {
      signedData = signTransaction(
          nonce, gasPrice, gasLimit, contract, value, data, chainId, privateKey);
      EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedData).send();
      log.info("交易哈希: " + ethSendTransaction.getTransactionHash());
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
  }

  /**
   * 创建钱包
   *
   * @param password 密码
   */
  public static WalletFile createWallet(String password) throws Exception {
    WalletFile walletFile;
    ECKeyPair ecKeyPair = Keys.createEcKeyPair();
    walletFile = Wallet.createStandard(password, ecKeyPair);

    return walletFile;
  }


  /**
   * 解密keystore 得到私钥
   *
   * @param keystore
   * @param password
   */
  public static String decryptWallet(String keystore, String password) {
    String privateKey = null;
    ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
    try {
      WalletFile walletFile = objectMapper.readValue(keystore, WalletFile.class);
      ECKeyPair ecKeyPair = Wallet.decrypt(password, walletFile);
      privateKey = ecKeyPair.getPrivateKey().toString(16);
      log.info(privateKey);
    } catch (Exception e) {
      log.warn(e.getMessage());
    }

    log.info("私钥: " + privateKey);
    return privateKey;
  }

  /** 签名交易 */
  public static String signTransaction(
      BigInteger nonce,
      BigInteger gasPrice,
      BigInteger gasLimit,
      String to,
      BigInteger value,
      String data,
      long chainId,
      String privateKey) {
    byte[] signedMessage;
    RawTransaction rawTransaction =
        RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);

    if (privateKey.startsWith("0x")) {
      privateKey = privateKey.substring(2);
    }
    ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
    Credentials credentials = Credentials.create(ecKeyPair);

    if (chainId > ChainIdLong.NONE) {
      signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
    } else {
      signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
    }

    String hexValue = Numeric.toHexString(signedMessage);
    log.info("签名之后的交易数据: " + hexValue);

    return hexValue;
  }


  private static String sendTransaction(String from, String password, String to, BigDecimal amount) {
    //解锁有效时间，单位秒
    BigInteger unlockDuration = BigInteger.valueOf(60L);
    String txHash = null;

    try {
      PersonalUnlockAccount personalUnlockAccount =
          admin.personalUnlockAccount(from, password, unlockDuration).send();
      if (personalUnlockAccount.accountUnlocked()) {
        log.info("解锁成功");
        BigInteger value = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
        log.info("转账金额:" + value);
        Transaction transaction = makeTransaction(from, to, null, null, null, value);
        // 不是必须的 可以使用默认值
        BigInteger gasLimit = getTransactionGasLimit(transaction);
        log.info("gasLimit:" + gasLimit);
        // 不是必须的 缺省值就是正确的值
        BigInteger nonce = getTransactionNonce(from);
        log.info("nonce:" + nonce);
        // 该值为大部分矿工可接受的gasPrice
        BigInteger gasPrice = Convert.toWei(defaultGasPrice, Convert.Unit.GWEI).toBigInteger();
        log.info("gasPrice:" + gasPrice);
        transaction = makeTransaction(from, to, nonce, gasPrice, gasLimit, value);
        EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).send();
        txHash = ethSendTransaction.getTransactionHash();
      }
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
    log.info("tx hash " + txHash);
    return txHash;
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
   * 获取余额
   *
   * @param address 钱包地址
   * @return 余额
   */
  private static BigInteger getETHBalance(String address) {
    BigInteger ethBalance = null;
    try {
      EthGetBalance ethGetBalance =
          web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
      ethBalance = ethGetBalance.getBalance();
    } catch (Exception e) {
      log.warn(e.getMessage());
    }

    return ethBalance;
  }

  /**
   * 代币转账
   */
  public static String sendTokenTransaction(String from, String password, String contract, String to, BigInteger amount) {
    //解锁有效时间，单位秒
    BigInteger unlockDuration = BigInteger.valueOf(60L);
    String txHash = null;

    try {
      PersonalUnlockAccount personalUnlockAccount =
          admin.personalUnlockAccount(from, password, unlockDuration).send();
      if (personalUnlockAccount.accountUnlocked()) {
        String methodName = "transfer";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        Address tAddress = new Address(to);

        Uint256 value = new Uint256(amount);
        inputParameters.add(tAddress);
        inputParameters.add(value);

        TypeReference<Bool> typeReference = new TypeReference<Bool>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);

        EthGetTransactionCount ethGetTransactionCount =
            web3j.ethGetTransactionCount(from, DefaultBlockParameterName.PENDING).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        BigInteger gasPrice = CommonConstant.GAS_PRICE;
        BigInteger gasLimit = CommonConstant.GAS_LIMIT;

        Transaction transaction =
            Transaction.createFunctionCallTransaction(
                from, nonce, gasPrice, gasLimit, contract, data);

        EthSendTransaction ethSendTransaction =
            web3j.ethSendTransaction(transaction).sendAsync().get();
        txHash = ethSendTransaction.getTransactionHash();
      }
    } catch (Exception e) {
      log.warn(e.getMessage());
    }

    return txHash;
  }

  public static BigInteger getTokenBalance(String from, String contract) {

    String methodName = "balanceOf";
    List<Type> inputParameters = new ArrayList<>();
    List<TypeReference<?>> outputParameters = new ArrayList<>();
    Address addr = new Address(from);
    inputParameters.add(addr);

    TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
    };
    outputParameters.add(typeReference);
    Function function = new Function(methodName, inputParameters, outputParameters);
    String data = FunctionEncoder.encode(function);
    Transaction transaction =
        Transaction.createEthCallTransaction(from, contract, data);

    EthCall ethCall;
    BigInteger tokenBalance = BigInteger.ZERO;
    try {
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
      List<Type> results =
          FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      tokenBalance = (BigInteger) results.get(0).getValue();
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
    return tokenBalance;
  }
}
