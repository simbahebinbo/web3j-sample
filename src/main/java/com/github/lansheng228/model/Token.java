package com.github.lansheng228.model;

public class Token {

    public String contractAddress;
    public int decimals;
    public String name;

    public Token(String contractAddress) {
        this.contractAddress = contractAddress;
        this.decimals = 0;
    }

    public Token(String contractAddress, int decimals) {
        this.contractAddress = contractAddress;
        this.decimals = decimals;
    }
}

