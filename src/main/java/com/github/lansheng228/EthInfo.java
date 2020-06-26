package com.github.lansheng228;

import java.io.IOException;
import java.math.BigInteger;

import com.github.lansheng228.utils.Environment;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthCoinbase;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthHashrate;
import org.web3j.protocol.core.methods.response.EthMining;
import org.web3j.protocol.core.methods.response.EthProtocolVersion;
import org.web3j.protocol.core.methods.response.EthSyncing;
import org.web3j.protocol.core.methods.response.NetPeerCount;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

@Slf4j
public class EthInfo {
  private static Web3j web3j;

  public static void main(String[] args) {
    web3j = Web3j.build(Environment.getService());
    getEthInfo();
  }

  /** 请求区块链的信息 */
  private static void getEthInfo() {

    Web3ClientVersion web3ClientVersion;
    try {
      // 客户端版本
      web3ClientVersion = web3j.web3ClientVersion().send();
      String clientVersion = web3ClientVersion.getWeb3ClientVersion();
      log.info("clientVersion " + clientVersion);
      // 区块数量
      EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();
      BigInteger blockNumber = ethBlockNumber.getBlockNumber();
      log.info(String.valueOf(blockNumber));
      // 挖矿奖励账户
      EthCoinbase ethCoinbase = web3j.ethCoinbase().send();
      String coinbaseAddress = ethCoinbase.getAddress();
      log.info(coinbaseAddress);
      // 是否在同步区块
      EthSyncing ethSyncing = web3j.ethSyncing().send();
      boolean isSyncing = ethSyncing.isSyncing();
      log.info(String.valueOf(isSyncing));
      // 是否在挖矿
      EthMining ethMining = web3j.ethMining().send();
      boolean isMining = ethMining.isMining();
      log.info(String.valueOf(isMining));
      // 当前gas price
      EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
      BigInteger gasPrice = ethGasPrice.getGasPrice();
      log.info(String.valueOf(gasPrice));
      // 挖矿速度
      EthHashrate ethHashrate = web3j.ethHashrate().send();
      BigInteger hashRate = ethHashrate.getHashrate();
      log.info(String.valueOf(hashRate));
      // 协议版本
      EthProtocolVersion ethProtocolVersion = web3j.ethProtocolVersion().send();
      String protocolVersion = ethProtocolVersion.getProtocolVersion();
      log.info(protocolVersion);
      // 连接的节点数
      NetPeerCount netPeerCount = web3j.netPeerCount().send();
      BigInteger peerCount = netPeerCount.getQuantity();
      log.info(String.valueOf(peerCount));
    } catch (IOException e) {
      log.warn(e.getMessage());
    }
  }
}
