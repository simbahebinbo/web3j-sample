package com.github.lansheng228;

import com.github.lansheng228.utils.Environment;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

/**
 * 快速开始
 */
@Slf4j
public class QuickStart {

  public static void main(String[] args) {
    Web3j web3j = Web3j.build(Environment.getService());

    try {
      Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
      String clientVersion = web3ClientVersion.getWeb3ClientVersion();
      log.info("clientVersion " + clientVersion);
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
  }
}
