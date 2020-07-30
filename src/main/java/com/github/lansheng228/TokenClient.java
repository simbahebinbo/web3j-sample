package com.github.lansheng228;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.github.lansheng228.utils.Environment;
import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Convert;

/**
 * 基于ERC20的代币
 */
@Slf4j
public class TokenClient {

  private static Web3j web3j;

  private static Admin admin;

  private static String fromAddress = "0x7b1cc408fcb2de1d510c1bf46a329e9027db4112";

  private static String contractAddress = "0x4c1ae77bc2df45fb68b13fa1b4f000305209b0cb";

  private static String emptyAddress = "0x0000000000000000000000000000000000000000";

  public static void main(String[] args) {
    web3j = Web3j.build(Environment.getService());
    admin = Admin.build(Environment.getService());
    getTokenBalance(web3j, fromAddress, contractAddress);
    log.info(getTokenName(web3j, contractAddress));
    log.info(String.valueOf(getTokenDecimals(web3j, contractAddress)));
    log.info(getTokenSymbol(web3j, contractAddress));
    log.info(String.valueOf(getTokenTotalSupply(web3j, contractAddress)));
    log.info(
        sendTokenTransaction(
            fromAddress,
            "yzw",
            "0x6c0f49aF552F2326DD851b68832730CB7b6C0DaF",
            contractAddress,
            BigInteger.valueOf(100000)));
  }

  /**
   * 查询代币余额
   */
  public static BigInteger getTokenBalance(
      Web3j web3j, String fromAddress, String contractAddress) {

    String methodName = "balanceOf";
    List<Type> inputParameters = new ArrayList<>();
    List<TypeReference<?>> outputParameters = new ArrayList<>();
    Address address = new Address(fromAddress);
    inputParameters.add(address);

    TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
    };
    outputParameters.add(typeReference);
    Function function = new Function(methodName, inputParameters, outputParameters);
    String data = FunctionEncoder.encode(function);
    Transaction transaction =
        Transaction.createEthCallTransaction(fromAddress, contractAddress, data);

    EthCall ethCall;
    BigInteger balanceValue = BigInteger.ZERO;
    try {
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
      List<Type> results =
          FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      balanceValue = (BigInteger) results.get(0).getValue();
    } catch (IOException e) {
      log.warn(e.getMessage());
    }
    return balanceValue;
  }

  /**
   * 查询代币名称
   */
  public static String getTokenName(Web3j web3j, String contractAddress) {
    String methodName = "name";
    String name = null;
    String fromAddr = emptyAddress;
    List<Type> inputParameters = new ArrayList<>();
    List<TypeReference<?>> outputParameters = new ArrayList<>();

    TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
    };
    outputParameters.add(typeReference);

    Function function = new Function(methodName, inputParameters, outputParameters);

    String data = FunctionEncoder.encode(function);
    Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

    EthCall ethCall;
    try {
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      List<Type> results =
          FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      name = results.get(0).getValue().toString();
    } catch (InterruptedException | ExecutionException e) {
      log.warn(e.getMessage());
    }
    return name;
  }

  /**
   * 查询代币符号
   */
  public static String getTokenSymbol(Web3j web3j, String contractAddress) {
    String methodName = "symbol";
    String symbol = null;
    String fromAddr = emptyAddress;
    List<Type> inputParameters = new ArrayList<>();
    List<TypeReference<?>> outputParameters = new ArrayList<>();

    TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
    };
    outputParameters.add(typeReference);

    Function function = new Function(methodName, inputParameters, outputParameters);

    String data = FunctionEncoder.encode(function);
    Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

    EthCall ethCall;
    try {
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      List<Type> results =
          FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      symbol = results.get(0).getValue().toString();
    } catch (InterruptedException | ExecutionException e) {
      log.warn(e.getMessage());
    }
    return symbol;
  }

  /**
   * 查询代币精度
   */
  public static int getTokenDecimals(Web3j web3j, String contractAddress) {
    String methodName = "decimals";
    String fromAddr = emptyAddress;
    int decimal = 0;
    List<Type> inputParameters = new ArrayList<>();
    List<TypeReference<?>> outputParameters = new ArrayList<>();

    TypeReference<Uint8> typeReference = new TypeReference<Uint8>() {
    };
    outputParameters.add(typeReference);

    Function function = new Function(methodName, inputParameters, outputParameters);

    String data = FunctionEncoder.encode(function);
    Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

    EthCall ethCall;
    try {
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      List<Type> results =
          FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      decimal = Integer.parseInt(results.get(0).getValue().toString());
    } catch (InterruptedException | ExecutionException e) {
      log.warn(e.getMessage());
    }
    return decimal;
  }

  /**
   * 查询代币发行总量
   */
  public static BigInteger getTokenTotalSupply(Web3j web3j, String contractAddress) {
    String methodName = "totalSupply";
    String fromAddr = emptyAddress;
    BigInteger totalSupply = BigInteger.ZERO;
    List<Type> inputParameters = new ArrayList<>();
    List<TypeReference<?>> outputParameters = new ArrayList<>();

    TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
    };
    outputParameters.add(typeReference);

    Function function = new Function(methodName, inputParameters, outputParameters);

    String data = FunctionEncoder.encode(function);
    Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

    EthCall ethCall;
    try {
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      List<Type> results =
          FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      totalSupply = (BigInteger) results.get(0).getValue();
    } catch (InterruptedException | ExecutionException e) {
      log.warn(e.getMessage());
    }
    return totalSupply;
  }

  /**
   * 代币转账
   */
  public static String sendTokenTransaction(
      String fromAddress,
      String password,
      String toAddress,
      String contractAddress,
      BigInteger amount) {
    String txHash = null;

    try {
      PersonalUnlockAccount personalUnlockAccount =
          admin.personalUnlockAccount(fromAddress, password, BigInteger.valueOf(10)).send();
      if (personalUnlockAccount.accountUnlocked()) {
        String methodName = "transfer";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        Address tAddress = new Address(toAddress);

        Uint256 value = new Uint256(amount);
        inputParameters.add(tAddress);
        inputParameters.add(value);

        TypeReference<Bool> typeReference = new TypeReference<Bool>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);

        EthGetTransactionCount ethGetTransactionCount =
            web3j
                .ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING)
                .sendAsync()
                .get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        BigInteger gasPrice =
            Convert.toWei(BigDecimal.valueOf(5), Convert.Unit.GWEI).toBigInteger();

        Transaction transaction =
            Transaction.createFunctionCallTransaction(
                fromAddress, nonce, gasPrice, BigInteger.valueOf(60000), contractAddress, data);

        EthSendTransaction ethSendTransaction =
            web3j.ethSendTransaction(transaction).sendAsync().get();
        txHash = ethSendTransaction.getTransactionHash();
      }
    } catch (Exception e) {
      log.warn(e.getMessage());
    }

    return txHash;
  }
}

