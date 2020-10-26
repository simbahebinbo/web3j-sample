package com.github.lansheng228.model;

import java.util.List;


public class EthHDWallet {

  String privateKey;
  String publicKey;
  List<String> mnemonic;
  String mnemonicPath;
  String address;
  String keystore;


  public EthHDWallet(String privateKey, String publicKey, List<String> mnemonic, String mnemonicPath, String address, String keystore) {
    this.privateKey = privateKey;
    this.publicKey = publicKey;
    this.mnemonic = mnemonic;
    this.mnemonicPath = mnemonicPath;
    this.address = address;
    this.keystore = keystore;
  }
}

