package com.github.lansheng228.utils;

import org.web3j.protocol.http.HttpService;

/**
 * 运行配置项
 */
public class Environment {

  public static String URL = "http://172.18.18.141:8545";

  /**
   * 通过http连接到geth节点
   */
  public static HttpService getService() {
    return new HttpService(URL);
  }
}
