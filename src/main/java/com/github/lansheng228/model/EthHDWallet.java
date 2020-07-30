package com.github.lansheng228.model;

import java.util.List;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class EthHDWallet {

  String privateKey;
  String publicKey;
  List<String> mnemonic;
  String mnemonicPath;
  String address;
  String keystore;
}
