package com.github.lansheng228;

import java.util.Arrays;
import java.util.List;

import com.github.lansheng228.utils.Environment;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;


/**
 * Event log 相关 监听合约event
 */
@Slf4j
public class ContractEvent {

  private static String contractAddress = "0x1ba45da3df0db8d37aeaa22f92f40fa60fbe1a09";
  private static Web3j web3j;

  public static void main(String[] args) {
    web3j = Web3j.build(Environment.getService());
    /**
     * 监听ERC20 token 交易
     */
    EthFilter filter = new EthFilter(
        DefaultBlockParameterName.EARLIEST,
        DefaultBlockParameterName.LATEST,
        contractAddress);
    Event event = new Event("Transfer",
        Arrays.asList(
            new TypeReference<Address>(true) {
            },
            new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>(false) {
            }
        )
    );

    String topicData = EventEncoder.encode(event);
    filter.addSingleTopic(topicData);
    log.info(topicData);

    Disposable subscription = web3j.ethLogFlowable(filter)
        .subscribe(ethLog -> {
          log.info("block number:  " + ethLog.getBlockNumber());
          log.info("transaction txHash: " + ethLog.getTransactionHash());
          List<String> topics = ethLog.getTopics();
          for (String topic : topics) {
            log.info("topic: " + topic);
          }
        });
  }
}

