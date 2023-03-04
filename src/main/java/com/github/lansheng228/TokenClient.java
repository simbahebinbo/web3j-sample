package com.github.lansheng228;

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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于ERC20的代币
 */
@Slf4j
public class TokenClient {

    private static final String fromAddress = "0xb4352408a1fAa75f49256D7E0665292d164F608c";
    private static final String toAddress = "0xB0031507C4800AFFe12AAF070Da60C273b097a3A";
    private static final String fromPassword = "123";
    private static final String contractAddress = "0x1ba45da3df0db8d37aeaa22f92f40fa60fbe1a09";
    private static final String emptyAddress = "0x0000000000000000000000000000000000000000";
    private static Web3j web3j;
    private static Admin admin;

    public static void main(String[] args) {
        web3j = Web3j.build(Environment.getService());
        admin = Admin.build(Environment.getService());
        log.info("token balance: " + getTokenBalance(fromAddress));
        log.info("token name: " + getTokenName());
        log.info("token decimals: " + getTokenDecimals());
        log.info("token symbol: " + getTokenSymbol());
        log.info("token totalSupply: " + getTokenTotalSupply());
        log.info("token transaction: " + sendTokenTransaction(fromAddress, fromPassword, toAddress, BigInteger.valueOf(1)));
    }

    /**
     * 查询代币余额
     */
    public static BigInteger getTokenBalance(String fromAddr) {
        return getTokenBalance(web3j, fromAddr, contractAddress);
    }

    public static BigInteger getTokenBalance(Web3j web3j, String fromAddr, String contractAddr) {

        String methodName = "balanceOf";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address addr = new Address(fromAddr);
        inputParameters.add(addr);

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction =
                Transaction.createEthCallTransaction(fromAddr, contractAddr, data);

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

    /**
     * 查询代币名称
     */
    public static String getTokenName() {
        return getTokenName(web3j, contractAddress);
    }

    public static String getTokenName(Web3j web3j, String contractAddr) {
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
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddr, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results =
                    FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            name = results.get(0).getValue().toString();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return name;
    }

    /**
     * 查询代币符号
     */
    public static String getTokenSymbol() {
        return getTokenSymbol(web3j, contractAddress);
    }

    public static String getTokenSymbol(Web3j web3j, String contractAddr) {
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
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddr, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results =
                    FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            symbol = results.get(0).getValue().toString();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return symbol;
    }

    /**
     * 查询代币精度
     */
    public static int getTokenDecimals() {
        return getTokenDecimals(web3j, contractAddress);
    }

    public static int getTokenDecimals(Web3j web3j, String contractAddr) {
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
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddr, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results =
                    FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            decimal = Integer.parseInt(results.get(0).getValue().toString());
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return decimal;
    }

    /**
     * 查询代币发行总量
     */
    public static BigInteger getTokenTotalSupply() {
        return getTokenTotalSupply(web3j, contractAddress);
    }

    public static BigInteger getTokenTotalSupply(Web3j web3j, String contractAddr) {
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
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddr, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results =
                    FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            totalSupply = (BigInteger) results.get(0).getValue();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        return totalSupply;
    }

    /**
     * 代币转账
     */
    public static String sendTokenTransaction(String fromAddr, String passwd, String toAddr, BigInteger amount) {
        return sendTokenTransaction(fromAddr, passwd, toAddr, contractAddress, amount);
    }

    public static String sendTokenTransaction(String fromAddr, String passwd, String toAddr, String contractAddr, BigInteger amount) {
        String txHash = null;

        try {
            PersonalUnlockAccount personalUnlockAccount =
                    admin.personalUnlockAccount(fromAddr, passwd, BigInteger.valueOf(10)).send();
            if (personalUnlockAccount.accountUnlocked()) {
                String methodName = "transfer";
                List<Type> inputParameters = new ArrayList<>();
                List<TypeReference<?>> outputParameters = new ArrayList<>();

                Address tAddress = new Address(toAddr);

                Uint256 value = new Uint256(amount);
                inputParameters.add(tAddress);
                inputParameters.add(value);

                TypeReference<Bool> typeReference = new TypeReference<Bool>() {
                };
                outputParameters.add(typeReference);

                Function function = new Function(methodName, inputParameters, outputParameters);

                String data = FunctionEncoder.encode(function);

                EthGetTransactionCount ethGetTransactionCount =
                        web3j.ethGetTransactionCount(fromAddr, DefaultBlockParameterName.PENDING).sendAsync().get();
                BigInteger nonce = ethGetTransactionCount.getTransactionCount();
                BigInteger gasPrice =
                        Convert.toWei(BigDecimal.valueOf(5), Convert.Unit.GWEI).toBigInteger();
                BigInteger gasLimit = BigInteger.valueOf(60000);

                Transaction transaction =
                        Transaction.createFunctionCallTransaction(
                                fromAddr, nonce, gasPrice, gasLimit, contractAddr, data);

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

