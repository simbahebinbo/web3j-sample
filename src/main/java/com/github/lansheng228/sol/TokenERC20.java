package com.github.lansheng228.sol;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.16.
 */
@SuppressWarnings("rawtypes")
public class TokenERC20 extends Contract {

  public static final String BINARY = "60806040526002805460ff191660121790553480156200001e57600080fd5b5060405162000b8d38038062000b8d8339810160408190526200004191620001d9565b60025460ff16600a0a830260038190553360009081526004602090815260408220929092558351620000769285019062000096565b5080516200008c90600190602084019062000096565b505050506200024a565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620000d957805160ff191683800117855562000109565b8280016001018555821562000109579182015b8281111562000109578251825591602001919060010190620000ec565b50620001179291506200011b565b5090565b5b808211156200011757600081556001016200011c565b600082601f83011262000143578081fd5b81516001600160401b03808211156200015a578283fd5b6040516020601f8401601f19168201810183811183821017156200017c578586fd5b806040525081945083825286818588010111156200019957600080fd5b600092505b83831015620001bd57858301810151828401820152918201916200019e565b83831115620001cf5760008185840101525b5050505092915050565b600080600060608486031215620001ee578283fd5b835160208501519093506001600160401b03808211156200020d578384fd5b6200021b8783880162000132565b9350604086015191508082111562000231578283fd5b50620002408682870162000132565b9150509250925092565b610933806200025a6000396000f3fe608060405234801561001057600080fd5b50600436106100b45760003560e01c806370a082311161007157806370a082311461014757806379cc67901461015a57806395d89b411461016d578063a9059cbb14610175578063cae9ca511461018a578063dd62ed3e1461019d576100b4565b806306fdde03146100b9578063095ea7b3146100d757806318160ddd146100f757806323b872dd1461010c578063313ce5671461011f57806342966c6814610134575b600080fd5b6100c16101b0565b6040516100ce9190610888565b60405180910390f35b6100ea6100e5366004610709565b61023e565b6040516100ce919061087d565b6100ff61026d565b6040516100ce919061089b565b6100ea61011a3660046106c9565b610273565b6101276102e3565b6040516100ce91906108a4565b6100ea6101423660046107dd565b6102ec565b6100ff61015536600461067a565b610367565b6100ea610168366004610709565b610379565b6100c1610450565b610188610183366004610709565b6104aa565b005b6100ea610198366004610733565b6104b9565b6100ff6101ab366004610695565b610541565b6000805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156102365780601f1061020b57610100808354040283529160200191610236565b820191906000526020600020905b81548152906001019060200180831161021957829003601f168201915b505050505081565b3360009081526005602090815260408083206001600160a01b0386168452909152902081905560015b92915050565b60035481565b6001600160a01b03831660009081526005602090815260408083203384529091528120548211156102a357600080fd5b6001600160a01b03841660009081526005602090815260408083203384529091529020805483900390556102d884848461055e565b5060015b9392505050565b60025460ff1681565b3360009081526004602052604081205482111561030857600080fd5b336000818152600460205260409081902080548590039055600380548590039055517fcc16f5dbb4873280815c1ee09dbd06736cffcc184412cf7a71a0fdb75d397ca59061035790859061089b565b60405180910390a2506001919050565b60046020526000908152604090205481565b6001600160a01b03821660009081526004602052604081205482111561039e57600080fd5b6001600160a01b03831660009081526005602090815260408083203384529091529020548211156103ce57600080fd5b6001600160a01b038316600081815260046020908152604080832080548790039055600582528083203384529091529081902080548590039055600380548590039055517fcc16f5dbb4873280815c1ee09dbd06736cffcc184412cf7a71a0fdb75d397ca59061043f90859061089b565b60405180910390a250600192915050565b60018054604080516020600284861615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156102365780601f1061020b57610100808354040283529160200191610236565b6104b533838361055e565b5050565b6000836104c6818561023e565b1561053957604051638f4ffcb160e01b81526001600160a01b03821690638f4ffcb1906104fd903390889030908990600401610840565b600060405180830381600087803b15801561051757600080fd5b505af115801561052b573d6000803e3d6000fd5b5050505060019150506102dc565b509392505050565b600560209081526000928352604080842090915290825290205481565b6001600160a01b03821661057157600080fd5b6001600160a01b03831660009081526004602052604090205481111561059657600080fd5b6001600160a01b038216600090815260046020526040902054818101116105bc57600080fd5b6001600160a01b0380831660008181526004602052604080822080549488168084528284208054888103909155938590528154870190915590519190930192907fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9061062990869061089b565b60405180910390a36001600160a01b0380841660009081526004602052604080822054928716825290205401811461065d57fe5b50505050565b80356001600160a01b038116811461026757600080fd5b60006020828403121561068b578081fd5b6102dc8383610663565b600080604083850312156106a7578081fd5b6106b18484610663565b91506106c08460208501610663565b90509250929050565b6000806000606084860312156106dd578081fd5b83356106e8816108e5565b925060208401356106f8816108e5565b929592945050506040919091013590565b6000806040838503121561071b578182fd5b6107258484610663565b946020939093013593505050565b600080600060608486031215610747578283fd5b8335610752816108e5565b925060208401359150604084013567ffffffffffffffff80821115610775578283fd5b818601915086601f830112610788578283fd5b813581811115610796578384fd5b6107a9601f8201601f19166020016108b2565b91508082528760208285010111156107bf578384fd5b6107d08160208401602086016108d9565b5080925050509250925092565b6000602082840312156107ee578081fd5b5035919050565b60008151808452815b8181101561081a576020818501810151868301820152016107fe565b8181111561082b5782602083870101525b50601f01601f19169290920160200192915050565b6001600160a01b0385811682526020820185905283166040820152608060608201819052600090610873908301846107f5565b9695505050505050565b901515815260200190565b6000602082526102dc60208301846107f5565b90815260200190565b60ff91909116815260200190565b60405181810167ffffffffffffffff811182821017156108d157600080fd5b604052919050565b82818337506000910152565b6001600160a01b03811681146108fa57600080fd5b5056fea2646970667358221220ec66fc3ecd349bf1fcec8766d3a3903a65bc878547c77208053a9142d39aa42464736f6c63430007000033";

  public static final String FUNC_ALLOWANCE = "allowance";

  public static final String FUNC_APPROVE = "approve";

  public static final String FUNC_APPROVEANDCALL = "approveAndCall";

  public static final String FUNC_BALANCEOF = "balanceOf";

  public static final String FUNC_BURN = "burn";

  public static final String FUNC_BURNFROM = "burnFrom";

  public static final String FUNC_DECIMALS = "decimals";

  public static final String FUNC_NAME = "name";

  public static final String FUNC_SYMBOL = "symbol";

  public static final String FUNC_TOTALSUPPLY = "totalSupply";

  public static final String FUNC_TRANSFER = "transfer";

  public static final String FUNC_TRANSFERFROM = "transferFrom";

  public static final Event BURN_EVENT = new Event("Burn",
      Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
      }, new TypeReference<Uint256>() {
      }));
  ;

  public static final Event TRANSFER_EVENT = new Event("Transfer",
      Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
      }, new TypeReference<Address>(true) {
      }, new TypeReference<Uint256>() {
      }));
  ;

  @Deprecated
  protected TokenERC20(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
    super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
  }

  protected TokenERC20(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
    super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
  }

  @Deprecated
  protected TokenERC20(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
    super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
  }

  protected TokenERC20(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
    super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
  }

  public List<BurnEventResponse> getBurnEvents(TransactionReceipt transactionReceipt) {
    List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(BURN_EVENT, transactionReceipt);
    ArrayList<BurnEventResponse> responses = new ArrayList<BurnEventResponse>(valueList.size());
    for (Contract.EventValuesWithLog eventValues : valueList) {
      BurnEventResponse typedResponse = new BurnEventResponse();
      typedResponse.log = eventValues.getLog();
      typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
      typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
      responses.add(typedResponse);
    }
    return responses;
  }

  public Flowable<BurnEventResponse> burnEventFlowable(EthFilter filter) {
    return web3j.ethLogFlowable(filter).map(new Function<Log, BurnEventResponse>() {
      @Override
      public BurnEventResponse apply(Log log) {
        Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(BURN_EVENT, log);
        BurnEventResponse typedResponse = new BurnEventResponse();
        typedResponse.log = log;
        typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
      }
    });
  }

  public Flowable<BurnEventResponse> burnEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
    EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
    filter.addSingleTopic(EventEncoder.encode(BURN_EVENT));
    return burnEventFlowable(filter);
  }

  public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
    List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
    ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
    for (Contract.EventValuesWithLog eventValues : valueList) {
      TransferEventResponse typedResponse = new TransferEventResponse();
      typedResponse.log = eventValues.getLog();
      typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
      typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
      typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
      responses.add(typedResponse);
    }
    return responses;
  }

  public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
    return web3j.ethLogFlowable(filter).map(new Function<Log, TransferEventResponse>() {
      @Override
      public TransferEventResponse apply(Log log) {
        Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
        TransferEventResponse typedResponse = new TransferEventResponse();
        typedResponse.log = log;
        typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
      }
    });
  }

  public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
    EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
    filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
    return transferEventFlowable(filter);
  }

  public RemoteFunctionCall<BigInteger> allowance(String param0, String param1) {
    final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ALLOWANCE,
        Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0),
            new org.web3j.abi.datatypes.Address(160, param1)),
        Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
        }));
    return executeRemoteCallSingleValueReturn(function, BigInteger.class);
  }

  public RemoteFunctionCall<TransactionReceipt> approve(String _spender, BigInteger _value) {
    final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
        FUNC_APPROVE,
        Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _spender),
            new org.web3j.abi.datatypes.generated.Uint256(_value)),
        Collections.<TypeReference<?>>emptyList());
    return executeRemoteCallTransaction(function);
  }

  public RemoteFunctionCall<TransactionReceipt> approveAndCall(String _spender, BigInteger _value, byte[] _extraData) {
    final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
        FUNC_APPROVEANDCALL,
        Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _spender),
            new org.web3j.abi.datatypes.generated.Uint256(_value),
            new org.web3j.abi.datatypes.DynamicBytes(_extraData)),
        Collections.<TypeReference<?>>emptyList());
    return executeRemoteCallTransaction(function);
  }

  public RemoteFunctionCall<BigInteger> balanceOf(String param0) {
    final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_BALANCEOF,
        Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)),
        Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
        }));
    return executeRemoteCallSingleValueReturn(function, BigInteger.class);
  }

  public RemoteFunctionCall<TransactionReceipt> burn(BigInteger _value) {
    final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
        FUNC_BURN,
        Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_value)),
        Collections.<TypeReference<?>>emptyList());
    return executeRemoteCallTransaction(function);
  }

  public RemoteFunctionCall<TransactionReceipt> burnFrom(String _from, BigInteger _value) {
    final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
        FUNC_BURNFROM,
        Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _from),
            new org.web3j.abi.datatypes.generated.Uint256(_value)),
        Collections.<TypeReference<?>>emptyList());
    return executeRemoteCallTransaction(function);
  }

  public RemoteFunctionCall<BigInteger> decimals() {
    final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DECIMALS,
        Arrays.<Type>asList(),
        Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {
        }));
    return executeRemoteCallSingleValueReturn(function, BigInteger.class);
  }

  public RemoteFunctionCall<String> name() {
    final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NAME,
        Arrays.<Type>asList(),
        Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
        }));
    return executeRemoteCallSingleValueReturn(function, String.class);
  }

  public RemoteFunctionCall<String> symbol() {
    final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SYMBOL,
        Arrays.<Type>asList(),
        Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
        }));
    return executeRemoteCallSingleValueReturn(function, String.class);
  }

  public RemoteFunctionCall<BigInteger> totalSupply() {
    final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOTALSUPPLY,
        Arrays.<Type>asList(),
        Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
        }));
    return executeRemoteCallSingleValueReturn(function, BigInteger.class);
  }

  public RemoteFunctionCall<TransactionReceipt> transfer(String _to, BigInteger _value) {
    final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
        FUNC_TRANSFER,
        Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _to),
            new org.web3j.abi.datatypes.generated.Uint256(_value)),
        Collections.<TypeReference<?>>emptyList());
    return executeRemoteCallTransaction(function);
  }

  public RemoteFunctionCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _value) {
    final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
        FUNC_TRANSFERFROM,
        Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _from),
            new org.web3j.abi.datatypes.Address(160, _to),
            new org.web3j.abi.datatypes.generated.Uint256(_value)),
        Collections.<TypeReference<?>>emptyList());
    return executeRemoteCallTransaction(function);
  }

  @Deprecated
  public static TokenERC20 load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
    return new TokenERC20(contractAddress, web3j, credentials, gasPrice, gasLimit);
  }

  @Deprecated
  public static TokenERC20 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice,
      BigInteger gasLimit) {
    return new TokenERC20(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
  }

  public static TokenERC20 load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
    return new TokenERC20(contractAddress, web3j, credentials, contractGasProvider);
  }

  public static TokenERC20 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
    return new TokenERC20(contractAddress, web3j, transactionManager, contractGasProvider);
  }

  public static RemoteCall<TokenERC20> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger initialSupply,
      String tokenName, String tokenSymbol) {
    String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(initialSupply),
        new org.web3j.abi.datatypes.Utf8String(tokenName),
        new org.web3j.abi.datatypes.Utf8String(tokenSymbol)));
    return deployRemoteCall(TokenERC20.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
  }

  public static RemoteCall<TokenERC20> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider,
      BigInteger initialSupply, String tokenName, String tokenSymbol) {
    String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(initialSupply),
        new org.web3j.abi.datatypes.Utf8String(tokenName),
        new org.web3j.abi.datatypes.Utf8String(tokenSymbol)));
    return deployRemoteCall(TokenERC20.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
  }

  @Deprecated
  public static RemoteCall<TokenERC20> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit,
      BigInteger initialSupply, String tokenName, String tokenSymbol) {
    String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(initialSupply),
        new org.web3j.abi.datatypes.Utf8String(tokenName),
        new org.web3j.abi.datatypes.Utf8String(tokenSymbol)));
    return deployRemoteCall(TokenERC20.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
  }

  @Deprecated
  public static RemoteCall<TokenERC20> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit,
      BigInteger initialSupply, String tokenName, String tokenSymbol) {
    String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(initialSupply),
        new org.web3j.abi.datatypes.Utf8String(tokenName),
        new org.web3j.abi.datatypes.Utf8String(tokenSymbol)));
    return deployRemoteCall(TokenERC20.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
  }

  public static class BurnEventResponse extends BaseEventResponse {

    public String from;

    public BigInteger value;
  }

  public static class TransferEventResponse extends BaseEventResponse {

    public String from;

    public String to;

    public BigInteger value;
  }
}
