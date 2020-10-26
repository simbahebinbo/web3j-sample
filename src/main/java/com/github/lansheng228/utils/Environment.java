package com.github.lansheng228.utils;

import com.github.lansheng228.common.CommonConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.web3j.protocol.http.HttpService;

/**
 * 运行配置项
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Environment {
  /**
   * 通过http连接到geth节点
   */
  public static HttpService getService() {
    return new HttpService(CommonConstant.URL);
  }
}
