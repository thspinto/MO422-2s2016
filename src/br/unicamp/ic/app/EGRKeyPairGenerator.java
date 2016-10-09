package br.unicamp.ic.app;

import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import java.math.BigInteger;
import java.security.*;

public class EGRKeyPairGenerator extends KeyPairGenerator {
  private SecureRandom random;
  private int bitLength;
  private static BigInteger THREE = BigInteger.valueOf(3);
  private static BigInteger FOUR = BigInteger.valueOf(4);

  /**
   * Creates a KeyPairGenerator for ElGamalRabin algorithm.
   */
  public EGRKeyPairGenerator() {
    super(Properties.name);
  }

  public void initialize(int bitLength, SecureRandom random) {
    this.random = random;
    this.bitLength = bitLength;
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
      kpg.initialize(2048, random);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    //Generate diffie-hellman keys
    KeyPair dhkp = kpg.generateKeyPair();
    DHParameterSpec dhParams =  ((DHPrivateKey) dhkp.getPrivate()).getParams();

    //Save private
    privateKey.setA(((DHPrivateKey) dhkp.getPrivate()).getX());
    privateKey.setR(dhParams.getP());
    privateKey.setG(dhParams.getG());

    //Save public
    publicKey.setGamma(((DHPublicKey) dhkp.getPublic()).getY());
    publicKey.setR(privateKey.getR());
    publicKey.setG(privateKey.getG());

    return new KeyPair(publicKey, privateKey);
  }

  /**
   * Generates a random prime such that p ≡ 3 (mod 4)
   *
   * @return a random blum prime
   */
  private BigInteger getBlumPrime() {
    BigInteger blumPrimeCandidate;
    do {
      blumPrimeCandidate = BigInteger.probablePrime(bitLength / 2, random);
    }
    while (!blumPrimeCandidate.mod(FOUR).equals(THREE));
    return blumPrimeCandidate;
  }
}
