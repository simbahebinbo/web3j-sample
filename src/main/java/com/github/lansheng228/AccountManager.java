package com.github.lansheng228;

import com.github.lansheng228.utils.Environment;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

/**
 * 账号管理相关
 */
@Slf4j
public class AccountManager {

    private static Admin admin;

    public static void main(String[] args) {
        admin = Admin.build(Environment.getService());
        String password = "123456789";
        String address = createNewAccount(password);
        getAccountList();
        unlockAccount(address, password);
    }

    /**
     * 创建账号
     */
    private static String createNewAccount(String password) {
        String address = null;
        try {
            NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(password).send();
            address = newAccountIdentifier.getAccountId();
            log.info("new account address " + address);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        return address;
    }

    /**
     * 获取账号列表
     */
    private static void getAccountList() {
        try {
            PersonalListAccounts personalListAccounts = admin.personalListAccounts().send();
            List<String> addressList;
            addressList = personalListAccounts.getAccountIds();
            if (Objects.nonNull(addressList)) {
                log.info("account size " + addressList.size());
                for (String address : addressList) {
                    log.info(address);
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * 账号解锁
     */
    private static void unlockAccount(String address, String password) {
        // 账号解锁持续时间 单位秒 缺省值300秒
        BigInteger unlockDuration = BigInteger.valueOf(60L);
        try {
            PersonalUnlockAccount personalUnlockAccount =
                    admin.personalUnlockAccount(address, password, unlockDuration).send();
            Boolean isUnlocked = personalUnlockAccount.accountUnlocked();
            log.info("account unlock " + isUnlocked);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
