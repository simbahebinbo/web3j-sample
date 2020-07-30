package com.github.lansheng228;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.github.lansheng228.model.Token;
import com.github.lansheng228.utils.Environment;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.Web3j;


/**
 * 批量查询token余额
 */
@Slf4j
public class TokenBalanceTask {

  private static Web3j web3j;

  // 要查询的token合约地址
  private static List<Token> tokenList;

  // 要查询的钱包地址
  private static List<String> addressList;

  public static void main(String[] args) {
    web3j = Web3j.build(Environment.getService());
    loadData();
    requestDecimals();
    requestName();
    processTask();
  }

  private static void loadData() {
    tokenList = new ArrayList<>();
    String contractAddress = "0x1ba45da3df0db8d37aeaa22f92f40fa60fbe1a09";
    Token token = new Token(contractAddress);
    tokenList.add(token);

    addressList = new ArrayList<>();
    String fromAddress = "0xb4352408a1fAa75f49256D7E0665292d164F608c";
    String toAddress = "0xB0031507C4800AFFe12AAF070Da60C273b097a3A";
    addressList.add(fromAddress);
    addressList.add(toAddress);
  }

  private static void requestDecimals() {
    for (Token token : tokenList) {
      token.decimals = TokenClient.getTokenDecimals(web3j, token.contractAddress);
      log.info("token decimals: " + token.decimals);
    }
  }

  private static void requestName() {
    for (Token token : tokenList) {
      token.name = TokenClient.getTokenName(web3j, token.contractAddress);
      log.info("token name: " + token.name);
    }
  }

  private static void processTask() {
    for (String address : addressList) {
      for (Token token : tokenList) {
        BigDecimal balance =
            new BigDecimal(TokenClient.getTokenBalance(web3j, address, token.contractAddress));
        balance.divide(BigDecimal.TEN.pow(token.decimals));
        log.info("address:  " + address + " name: " + token.name + " balance: " + balance);
      }
    }
  }
}
