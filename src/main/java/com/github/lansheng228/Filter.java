package com.github.lansheng228;

import com.github.lansheng228.utils.Environment;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


/**
 * filter 相关
 * <p>
 * 监听区块、交易
 * <p>
 * 所有监听都在Web3jRx中
 */
@Slf4j
public class Filter {

    private static Web3j web3j;

    public static void main(String[] args) {
        web3j = Web3j.build(Environment.getService());

        newBlockSimpleFilter(web3j);

        newBlockFullFilter(web3j);

        newTransactionFilter(web3j);

        replayBlockSimpleFilter(web3j);

        replayBlockFullFilter(web3j);

        replayTransactionFilter(web3j);

        catchUpBlockSimpleFilter(web3j);

        catchUpBlockFullFilter(web3j);

        catchUpTransactionFilter(web3j);

        countingTransactionFilter(web3j);
    }


    /**
     * 新区块监听  详细信息
     */
    private static void newBlockSimpleFilter(Web3j web3j) {
        Disposable subscription = web3j.blockFlowable(false)
                .subscribe(ethBlock -> log.info("new block come in   " + "  block number  " + ethBlock.getBlock().getNumber()));
    }

    /**
     * 新区块监听  简略信息
     */
    private static void newBlockFullFilter(Web3j web3j) {

        Disposable subscription = web3j.blockFlowable(true)
                .subscribe(ethBlock -> {
                    EthBlock.Block block = ethBlock.getBlock();
                    LocalDateTime timestamp = Instant.ofEpochSecond(block.getTimestamp().longValueExact()).atZone(ZoneId.of("UTC")).toLocalDateTime();
                    int transactionCount = block.getTransactions().size();
                    String hash = block.getHash();
                    String parentHash = block.getParentHash();

                    log.info(timestamp + " " +
                            "Tx count: " + transactionCount + ", " +
                            "Hash: " + hash + ", " +
                            "Parent hash: " + parentHash);
                });
    }


    /**
     * 新交易监听
     */
    private static void newTransactionFilter(Web3j web3j) {
        Disposable subscription = web3j.transactionFlowable()
                .subscribe(transaction -> log.info("transaction come in   " + "  transaction txHash " + transaction.getHash()));
    }

    /**
     * 遍历旧区块
     * <p>
     * 区块简略信息
     */
    private static void replayBlockSimpleFilter(Web3j web3j) {
        BigInteger startBlock = BigInteger.valueOf(1);
        BigInteger endBlock = BigInteger.valueOf(5);

        Disposable subscription = web3j.
                replayPastBlocksFlowable(
                        DefaultBlockParameter.valueOf(startBlock),
                        DefaultBlockParameter.valueOf(endBlock),
                        false).
                subscribe(ethBlock -> log.info("replay block   " + "  block number  " + ethBlock.getBlock().getNumber()));
    }

    /**
     * 遍历旧区块
     * <p>
     * 区块详细信息
     */
    private static void replayBlockFullFilter(Web3j web3j) {
        BigInteger startBlock = BigInteger.valueOf(1);
        BigInteger endBlock = BigInteger.valueOf(5);

        Disposable subscription = web3j.
                replayPastBlocksFlowable(
                        DefaultBlockParameter.valueOf(startBlock),
                        DefaultBlockParameter.valueOf(endBlock),
                        true).
                subscribe(ethBlock -> {
                    EthBlock.Block block = ethBlock.getBlock();
                    LocalDateTime timestamp = Instant.ofEpochSecond(block.getTimestamp().longValueExact()).atZone(ZoneId.of("UTC")).toLocalDateTime();
                    int transactionCount = block.getTransactions().size();
                    String hash = block.getHash();
                    String parentHash = block.getParentHash();

                    log.info(timestamp + " " +
                            "Tx count: " + transactionCount + ", " +
                            "Hash: " + hash + ", " +
                            "Parent hash: " + parentHash);
                });
    }

    /**
     * 遍历旧交易
     */
    private static void replayTransactionFilter(Web3j web3j) {
        BigInteger startBlock = BigInteger.valueOf(1);
        BigInteger endBlock = BigInteger.valueOf(5);

        Disposable subscription = web3j.replayPastTransactionsFlowable(
                        DefaultBlockParameter.valueOf(startBlock),
                        DefaultBlockParameter.valueOf(endBlock)).
                subscribe(transaction -> log.info("replay transaction  " + "   txHash   " + transaction.getHash()));
    }

    /**
     * 从某一区块开始直到最新区块
     * <p>
     * 遍历旧区块，监听新区块
     * <p>
     * 区块简略信息
     */
    private static void catchUpBlockSimpleFilter(Web3j web3j) {
        BigInteger startBlock = BigInteger.valueOf(1);

        Disposable subscription = web3j.replayPastAndFutureBlocksFlowable(
                        DefaultBlockParameter.valueOf(startBlock), false)
                .subscribe(ethBlock -> log.info("block  " + "  block number  " + ethBlock.getBlock().getNumber()));
    }

    /**
     * 从某一区块开始直到最新区块
     * <p>
     * 遍历旧区块，监听新区块
     * <p>
     * 区块详细信息
     */
    private static void catchUpBlockFullFilter(Web3j web3j) {
        BigInteger startBlock = BigInteger.valueOf(1);

        Disposable subscription = web3j.replayPastAndFutureBlocksFlowable(
                        DefaultBlockParameter.valueOf(startBlock), true)
                .subscribe(ethBlock -> {
                    EthBlock.Block block = ethBlock.getBlock();
                    LocalDateTime timestamp = Instant.ofEpochSecond(block.getTimestamp().longValueExact()).atZone(ZoneId.of("UTC")).toLocalDateTime();
                    int transactionCount = block.getTransactions().size();
                    String hash = block.getHash();
                    String parentHash = block.getParentHash();

                    log.info(timestamp + " " +
                            "Tx count: " + transactionCount + ", " +
                            "Hash: " + hash + ", " +
                            "Parent hash: " + parentHash);
                });
    }

    /**
     * 从某一区块开始直到最新交易
     * <p>
     * 遍历旧交易，监听新交易
     */
    private static void catchUpTransactionFilter(Web3j web3j) {
        BigInteger startBlock = BigInteger.valueOf(1);

        Disposable subscription = web3j.replayPastAndFutureTransactionsFlowable(
                        DefaultBlockParameter.valueOf(startBlock))
                .subscribe(tx -> log.info("transaction" + "   txHash   " + tx.getHash()));
    }

    /**
     * 监听新交易并统计交易数额
     */
    private static void countingTransactionFilter(Web3j web3j) {
        Disposable subscription = web3j.transactionFlowable()
                .take(3)
                .map(Transaction::getValue)
                .reduce(BigInteger.ZERO, BigInteger::add)
                .subscribe(total -> {
                    BigDecimal value = new BigDecimal(total);
                    log.info("Transaction value: " + Convert.fromWei(value, Convert.Unit.ETHER) + " Ether (" + value + " Wei)");
                });
    }
}


