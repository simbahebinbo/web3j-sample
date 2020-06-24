//package com.github.lansheng228;
//
//import com.github.lansheng228.utils.Environment;
//import org.reactivestreams.Subscription;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.core.DefaultBlockParameter;
//import org.web3j.protocol.http.HttpService;
//
//import java.math.BigInteger;
//
//
///**
// * filter相关 监听区块、交易 所有监听都在Web3jRx中
// */
//public class Filter {
//
//  private static Web3j web3j;
//
//  public static void main(String[] args) {
//    web3j = Web3j.build(new HttpService(Environment.RPC_URL));
//    /**
//     * 新区块监听
//     */
//    newBlockFilter(web3j);
//    /**
//     * 新交易监听
//     */
//    newTransactionFilter(web3j);
//    /**
//     * 遍历旧区块、交易
//     */
//    replayFilter(web3j);
//    /**
//     * 从某一区块开始直到最新区块、交易
//     */
//    catchUpFilter(web3j);
//
//    /**
//     * 取消监听
//     */
//    //subscription.unsubscribe();
//  }
//
//  private static void newBlockFilter(Web3j web3j) {
//    Subscription subscription = web3j.
//        blockObservable(false).
//        subscribe(block -> {
//          log.info("new block come in");
//          log.info("block number" + block.getBlock().getNumber());
//        });
//  }
//
//  private static void newTransactionFilter(Web3j web3j) {
//    Subscription subscription = web3j.
//        transactionObservable().
//        subscribe(transaction -> {
//          log.info("transaction come in");
//          log.info("transaction txHash " + transaction.getHash());
//        });
//  }
//
//  private static void replayFilter(Web3j web3j) {
//    BigInteger startBlock = BigInteger.valueOf(2000000);
//    BigInteger endBlock = BigInteger.valueOf(2010000);
//    /**
//     * 遍历旧区块
//     */
//    Subscription subscription = web3j.
//        replayBlocksObservable(
//            DefaultBlockParameter.valueOf(startBlock),
//            DefaultBlockParameter.valueOf(endBlock),
//            false).
//        subscribe(ethBlock -> {
//          log.info("replay block");
//          log.info(ethBlock.getBlock().getNumber());
//        });
//
//    /**
//     * 遍历旧交易
//     */
//    Subscription subscription1 = web3j.
//        replayTransactionsObservable(
//            DefaultBlockParameter.valueOf(startBlock),
//            DefaultBlockParameter.valueOf(endBlock)).
//        subscribe(transaction -> {
//          log.info("replay transaction");
//          log.info("txHash " + transaction.getHash());
//        });
//  }
//
//  private static void catchUpFilter(Web3j web3j) {
//    BigInteger startBlock = BigInteger.valueOf(2000000);
//
//    /**
//     * 遍历旧区块，监听新区块
//     */
//    Subscription subscription = web3j.catchUpToLatestAndSubscribeToNewBlocksObservable(
//        DefaultBlockParameter.valueOf(startBlock), false)
//        .subscribe(block -> {
//          log.info("block");
//          log.info(block.getBlock().getNumber());
//        });
//
//    /**
//     * 遍历旧交易，监听新交易
//     */
//    Subscription subscription2 = web3j.catchUpToLatestAndSubscribeToNewTransactionsObservable(
//        DefaultBlockParameter.valueOf(startBlock))
//        .subscribe(tx -> {
//          log.info("transaction");
//          log.info(tx.getHash());
//        });
//  }
//}
