package br.unicamp.ic.app;

import java.math.BigInteger;
import java.security.*;
import java.util.Random;

public class Main {
  private static Random random = new SecureRandom();
  private static BigInteger TWO = BigInteger.valueOf(2);


  public static void  main(String args[]){
    Security.addProvider(new EGRProvider());

    KeyPairGenerator kpg = null;
    try {
      kpg = KeyPairGenerator.getInstance(Properties.name);
      kpg.initialize(Properties.bitLength, new SecureRandom());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    //Generate diffie-hellman keys


    //TODO: Save/load key files
    //TODO: Limit input size
    System.out.println("test");
  }

  private static BigInteger[] encrypt(BigInteger m, KeyPair keyPair){

    return null;
  }

  private static BigInteger decrypt(BigInteger c, KeyPair keyPair){

    return null;
  }
}

