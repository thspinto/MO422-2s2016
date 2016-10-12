package br.unicamp.ic.app;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Random;

import javax.crypto.Cipher;

public class Main {
  private static Random random = new SecureRandom();
  private static BigInteger TWO = BigInteger.valueOf(2);


  public static void main(String args[]) {
    Provider egrProvider = new EGRProvider();
    Security.addProvider(egrProvider);

    KeyPairGenerator kpg;
    EGREngine c = new EGREngine();
    try {
      kpg = KeyPairGenerator.getInstance(Properties.name);
      kpg.initialize(Properties.bitLength, new SecureRandom());
      KeyPair keyPair = kpg.generateKeyPair();
      c.engineInit(Cipher.ENCRYPT_MODE, keyPair.getPublic(), new SecureRandom());
      byte[] ct = c.encrypt(BigInteger.valueOf(45).toByteArray());
      EGRParameterSpec paramSpec = new EGRParameterSpec();
      paramSpec.setDh(new BigInteger(c.engineGetIV()));
      c.engineInit(Cipher.DECRYPT_MODE, keyPair.getPrivate(), paramSpec, new SecureRandom());
      c.decrypt(ct);
    } catch (Exception e) {
      e.printStackTrace();
    }


    //TODO: Save/load key files
    //TODO: Limit input size
  }

  private static BigInteger[] encrypt(BigInteger m, KeyPair keyPair) {

    return null;
  }

  private static BigInteger decrypt(BigInteger c, KeyPair keyPair) {

    return null;
  }
}

