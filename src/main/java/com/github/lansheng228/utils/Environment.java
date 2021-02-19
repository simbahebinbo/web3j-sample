package com.github.lansheng228.utils;

import com.github.lansheng228.common.CommonConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import org.apache.commons.lang3.StringUtils;
import org.web3j.protocol.http.HttpService;

/**
 * 运行配置项
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Environment {
  /**
   * 通过http连接到geth节点
   */
  public static HttpService getService() {
    HttpService httpService = new HttpService(CommonConstant.NODE_URL);
    log.info(
        "连接 ETH 节点 {}  的 用户名 {} 和 密码 {}  用户名是否为空: {}  密码是否为空: {} ",
        CommonConstant.NODE_URL,
        CommonConstant.NODE_USER,
        CommonConstant.NODE_PASSWORD,
        StringUtils.isEmpty(CommonConstant.NODE_USER),
        StringUtils.isEmpty(CommonConstant.NODE_PASSWORD));

    if (StringUtils.isNotEmpty(CommonConstant.NODE_USER) && StringUtils.isNotEmpty(CommonConstant.NODE_PASSWORD)) {
      log.info("登录 ETH 节点 需要验证 添加报文头");
      httpService.addHeader("Authorization", Credentials.basic(CommonConstant.NODE_USER, CommonConstant.NODE_PASSWORD));
    }

    return httpService;
  }
}
