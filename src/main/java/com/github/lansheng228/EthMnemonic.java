package com.github.lansheng228;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lansheng228.common.CommonConstant;
import com.github.lansheng228.model.EthHDWallet;
import com.github.lansheng228.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;
import org.bitcoinj.wallet.DeterministicSeed;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 以太坊助记词
 * 用到了比特币的jar包 org.bitcoinj
 * <p>
 * 生成mnemonic > 生成seed > 生成 Extended Public Key
 * 生成地址主要依赖Extended Public Key，加上addressIndex(0至232-1)就可以确定一个地址.
 * BTC使用m/44’/0’/0’/0的 Extended Public Key 生成 m/44’/0’/0’/0/*，
 * ETH使用m/44’/60’/0’/0的 Extended Public Key 生成 m/44’/60’/0’/0/*,
 * mainnet的Extended Public Key以xpub做前缀
 * 验证网址：https://iancoleman.io/bip39/
 */
@Slf4j
public class EthMnemonic {
    /**
     * 通用的以太坊基于bip44协议的助记词路径 （imtoken jaxx Metamask myetherwallet）
     */
    private static final String ETH_TYPE = "m/44'/60'/0'/0/0";

    private static final SecureRandom secureRandom = new SecureRandom();

    private static final String walletPassword = "11111111";


    public static void main(String[] args) {
        //生成助记词
        generateMnemonic(ETH_TYPE, walletPassword);

        //导入助记词
        //[team, bid, property, oval, hedgehog, observe, badge, cabin, color, cruel, casino, blame]
        List<String> mnemonicList = new ArrayList<>();
        mnemonicList.add("team");
        mnemonicList.add("bid");
        mnemonicList.add("property");
        mnemonicList.add("oval");
        mnemonicList.add("hedgehog");
        mnemonicList.add("observe");
        mnemonicList.add("badge");
        mnemonicList.add("cabin");
        mnemonicList.add("color");
        mnemonicList.add("cruel");
        mnemonicList.add("casino");
        mnemonicList.add("blame");
        importMnemonic(ETH_TYPE, mnemonicList, walletPassword);
    }

    public static EthHDWallet generateMnemonic(String path, String password) {
        if (!path.startsWith("m") && !path.startsWith("M")) {
            //参数非法
            log.warn("参数非法");
            return null;
        }
        String[] pathArray = path.split(CommonConstant.SEPARATOR_VIRGULE);
        if (pathArray.length <= 1) {
            //内容不对
            log.warn("内容不对");
            return null;
        }

        if (password.length() < 8) {
            //密码过短
            log.warn("密码过短");
            return null;
        }

        String passphrase = "";
        long creationTimeSeconds = DateUtil.currentTimeMillis() / 1000;
        DeterministicSeed ds = new DeterministicSeed(secureRandom, 128, passphrase);
        return createEthWallet(ds, pathArray, password);
    }

    private static EthHDWallet importMnemonic(String path, List<String> mnemonicList, String password) {
        if (!path.startsWith("m") && !path.startsWith("M")) {
            //参数非法
            log.warn("参数非法");
            return null;
        }

        String[] pathArray = path.split(CommonConstant.SEPARATOR_VIRGULE);
        if (pathArray.length <= 1) {
            //内容不对
            log.warn("内容不对");
            return null;
        }
        if (password.length() < 8) {
            //密码过短
            log.warn("密码过短");
            return null;
        }
        String passphrase = "";
        long creationTimeSeconds = DateUtil.currentTimeMillis() / 1000;
        DeterministicSeed ds = new DeterministicSeed(mnemonicList, null, passphrase, creationTimeSeconds);

        return createEthWallet(ds, pathArray, password);
    }

    private static EthHDWallet createEthWallet(DeterministicSeed ds, String[] pathArray, String password) {
        //根私钥
        byte[] seedBytes = ds.getSeedBytes();
        log.info("根私钥 " + Arrays.toString(seedBytes));
        //助记词
        List<String> mnemonic = ds.getMnemonicCode();
        log.info("助记词 " + mnemonic);

        try {
            //助记词种子
            byte[] mnemonicSeedBytes = MnemonicCode.INSTANCE.toEntropy(mnemonic);
            log.info("助记词种子 " + Arrays.toString(mnemonicSeedBytes));
            ECKeyPair mnemonicKeyPair = ECKeyPair.create(mnemonicSeedBytes);
            WalletFile walletFile = Wallet.createLight(password, mnemonicKeyPair);
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            //存这个keystore 用完后删除
            String jsonStr = objectMapper.writeValueAsString(walletFile);
            log.info("mnemonic keystore " + jsonStr);
            //验证
            WalletFile checkWalletFile = objectMapper.readValue(jsonStr, WalletFile.class);
            ECKeyPair ecKeyPair = Wallet.decrypt(password, checkWalletFile);
            byte[] checkMnemonicSeedBytes = Numeric.hexStringToByteArray(ecKeyPair.getPrivateKey().toString(16));
            log.info("验证助记词种子 " + Arrays.toString(checkMnemonicSeedBytes));
            List<String> checkMnemonic = MnemonicCode.INSTANCE.toMnemonic(checkMnemonicSeedBytes);
            log.info("验证助记词 " + checkMnemonic);

        } catch (MnemonicException.MnemonicLengthException | MnemonicException.MnemonicWordException |
                 MnemonicException.MnemonicChecksumException | CipherException | IOException e) {
            log.warn(e.getMessage());
        }

        if (seedBytes == null) {
            return null;
        }

        DeterministicKey dkKey = HDKeyDerivation.createMasterPrivateKey(seedBytes);
        for (int i = 1; i < pathArray.length; i++) {
            ChildNumber childNumber;
            if (pathArray[i].endsWith("'")) {
                int number = Integer.parseInt(pathArray[i].substring(0,
                        pathArray[i].length() - 1));
                childNumber = new ChildNumber(number, true);
            } else {
                int number = Integer.parseInt(pathArray[i]);
                childNumber = new ChildNumber(number, false);
            }
            dkKey = HDKeyDerivation.deriveChildKey(dkKey, childNumber);
        }
        log.info("path " + dkKey.getPathAsString());

        ECKeyPair keyPair = ECKeyPair.create(dkKey.getPrivKeyBytes());
        log.info("eth privateKey " + keyPair.getPrivateKey().toString(16));
        log.info("eth publicKey " + keyPair.getPublicKey().toString(16));

        EthHDWallet ethHDWallet = null;
        try {
            WalletFile walletFile = Wallet.createLight(password, keyPair);
            log.info("eth address " + "0x" + walletFile.getAddress());
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            //存
            String jsonStr = objectMapper.writeValueAsString(walletFile);
            log.info("eth keystore " + jsonStr);

            ethHDWallet = new EthHDWallet(keyPair.getPrivateKey().toString(16),
                    keyPair.getPublicKey().toString(16),
                    mnemonic, dkKey.getPathAsString(),
                    "0x" + walletFile.getAddress(), jsonStr);
        } catch (CipherException | JsonProcessingException e) {
            log.warn(e.getMessage());
        }

        return ethHDWallet;
    }
}
