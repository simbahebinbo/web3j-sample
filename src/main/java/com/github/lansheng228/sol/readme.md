### 编译合约
```
$ solc TokenERC20.sol --bin --abi --optimize -o $HOME --overwrite
$ solc UserManagement.sol --bin --abi --optimize -o $HOME --overwrite
```

### 生成java文件
```
$ web3j solidity generate --javaTypes -b $HOME/TokenERC20.bin -a $HOME/TokenERC20.abi -o $HOME -p com.github.lansheng228.sol
$ web3j solidity generate --javaTypes -b $HOME/UserManagement.bin -a $HOME/UserManagement.abi -o $HOME -p com.github.lansheng228.sol
```
