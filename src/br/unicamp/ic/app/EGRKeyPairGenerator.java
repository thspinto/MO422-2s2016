package br.unicamp.ic.app;

import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EGRKeyPairGenerator extends KeyPairGenerator {
  private static BigInteger THREE = BigInteger.valueOf(3);
  private static BigInteger FOUR = BigInteger.valueOf(4);

  /**
   * Creates a KeyPairGenerator for ElGamalRabin algorithm.
   */
  protected EGRKeyPairGenerator() {
    super("EGR");
  }

  public KeyPair generateKeyPair() {
    EGRPrivateKey privateKey = new EGRPrivateKey();
    EGRPublicKey publicKey = new EGRPublicKey();

    //Rabin private key
    privateKey.setP(getBlumPrime());
    privateKey.setQ(getBlumPrime());
    //Rabin public key p * q
    publicKey.setN(privateKey.getP().multiply(privateKey.getQ()));

    //Diffie-Hellman key exchange (Using ElGamal's idea)
    KeyPairGenerator kpg = null;
    try {
      kpg = KeyPairGenerator.getInstance("DH");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    kpg.initialize(Properties.bitLength, new SecureRandom());
    KeyPair dhkp = kpg.generateKeyPair();

    DHParameterSpec dhParams =  ((DHPrivateKey) dhkp.getPrivate()).getParams();
    privateKey.setA(((DHPrivateKey) dhkp.getPrivate()).getX());
    privateKey.setR(dhParams.getP());
    privateKey.setG(dhParams.getG());

    publicKey.setGamma(((DHPublicKey) dhkp.getPublic()).getY());
    publicKey.setR(privateKey.getR());
    publicKey.setG(privateKey.getG());

    KeyPair kp = new KeyPair(publicKey, privateKey);
    return kp;
  }

  /**
   * Generates a random prime such that p ≡ 3 (mod 4)
   *
   * @return a random blum prime
   */
  private BigInteger getBlumPrime() {
    BigInteger blumPrimeCandidate;
    do {
      blumPrimeCandidate = BigInteger.probablePrime(Properties.bitLength / 2, new SecureRandom());
    }
    while (!blumPrimeCandidate.mod(FOUR).equals(THREE));
    return blumPrimeCandidate;
  }
}
