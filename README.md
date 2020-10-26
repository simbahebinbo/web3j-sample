# web3j-sample
web3 for java 样例程序

###  依赖包

* web3j 5.0.0

* maven 3.6.3

###  安装 solidity 和 web3j
```
$ brew tap ethereum/ethereum
$ brew install solidity
$ brew tap web3j/web3j
$ brew install web3j
$ web3j version
4.5.16
$ solc --version 
0.7.0
```


运行前提 需要有一个开启RPC或者IPC服务的以太坊节点

*  [搭建以太坊私有链节点](https://lansheng228.github.io/posts/6d8a95bf/)

*  [搭建以太坊测试链节点](https://lansheng228.github.io/posts/fad837cb/)

本次开发使用私有链。

- [QuickStart](https://github.com/lansheng228/web3j-sample/blob/master/src/main/java/com/github/lansheng228/QuickStart.java) 快速开始
- [AccountManager](https://github.com/lansheng228/web3j-sample/blob/master/src/main/java/com/github/lansheng228/AccountManager.java) 账号相关接口
- [TransactionClient](https://github.com/lansheng228/web3j-sample/blob/master/src/main/java/com/github/lansheng228/TransactionClient.java) eth转账相关接口
- [EthInfo](https://github.com/lansheng228/web3j-sample/blob/master/src/main/java/com/github/lansheng228/EthInfo.java) 连接节点的相关信息接口
- [IBAN](https://github.com/lansheng228/web3j-sample/blob/master/src/main/java/com/github/lansheng228/IBAN.java) 根据官方规则生成iban及付款二维码
- [Calculate](https://github.com/lansheng228/web3j-sample/blob/master/src/main/java/com/github/lansheng228/Calculate.java) 在发布合约前计算合约地址，根据签名后的交易信息计算TxHash
- [DecodeMessage](https://github.com/lansheng228/web3j-sample/blob/master/src/main/java/com/github/lansheng228/DecodeMessage.java) 加密后的交易数据解析
- [Filter](https://github.com/lansheng228/web3j-sample/blob/master/src/main/java/com/github/lansheng228/Filter.java) 新块、新交易相关监听
- [Security](https://github.com/lansheng228/web3j-sample/blob/master/src/main/java/com/github/lansheng228/Security.java) 公钥私钥相关接口
- [Solidity](https://github.com/lansheng228/web3j-sample/tree/master/src/main/java/com/github/lansheng228/sol) 合约类相关
- [TokenClient](https://github.com/lansheng228/web3j-sample/blob/master/src/main/java/com/github/lansheng228/TokenClient.java) token代币相关查询及转账
- [TokenBalanceTask](https://github.com/lansheng228/web3j-sample/blob/master/src/main/java/com/github/lansheng228/TokenBalanceTask.java) 批量token代币余额查询
- [ContractEvent](https://github.com/lansheng228/web3j-sample/blob/master/src/main/java/com/github/lansheng228/ContractEvent.java) 执行合约相关log监听
- [ColdWallet](https://github.com/lansheng228/web3j-sample/blob/master/src/main/java/com/github/lansheng228/ColdWallet.java) 冷钱包创建、交易
- [EthMnemonic](https://github.com/lansheng228/web3j-sample/blob/master/src/main/java/com/github/lansheng228/EthMnemonic.java) 生成、导入助记词

