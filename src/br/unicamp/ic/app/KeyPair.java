package br.unicamp.ic.app;

import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class KeyPair {
  private static BigInteger TWO = BigInteger.valueOf(2);
  private static BigInteger THREE = BigInteger.valueOf(3);
  private static BigInteger FOUR = BigInteger.valueOf(4);
  private static BigInteger FIVE = BigInteger.valueOf(5);
  private static BigInteger TWELVE = BigInteger.valueOf(12);
  private EGRPublicKey privateKey;
  private EGRPublicKey publicKey;

  /**
   * Creates a new Public/Private key pair.
   */
  public KeyPair() throws NoSuchAlgorithmException {
    privateKey = new EGRPublicKey();
    publicKey = new EGRPublicKey();

    //Rabin private key
    privateKey.setP(getBlumPrime());
    privateKey.setQ(getBlumPrime());
    //Rabin public key p * q
    publicKey.setN(privateKey.getP().multiply(privateKey.getQ()));

    //Diffie-Hellman key exchange (Using ElGamal's idea)
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("DH");
    kpg.initialize(Properties.bitLength);
    java.security.KeyPair kp = kpg.generateKeyPair();


    BigInteger sophieGermainPrime = getSophieGermainPrime();


    BigInteger safePrime = sophieGermainPrime.multiply(TWO).add(BigInteger.ONE);
    publicKey.setR(safePrime);
    publicKey.setAlpha(TWO);

  }

  /**
   * Saves public and private keys to separate files.
   */
  public void save() {

  }

  /**
   * Loads public and private keys from files.
   */
  public void load() {

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

  /**
   * Generates Sophie Germain Prime.
   * A Sophie Germain prime p is a prime such that 2q + 1 is also prime.
   * <p>
   * Ref: https://www.ietf.org/rfc/rfc4419.txt
   *
   * @return a random safe prime
   */
  private BigInteger getSophieGermainPrime() {
    BigInteger sophieGermainPrimeCandidate;
    do {
      sophieGermainPrimeCandidate = BigInteger.probablePrime(Properties.bitLength / 2, new SecureRandom());
    }
    while (sophieGermainPrimeCandidate.mod(TWELVE).equals(FIVE) &&
            sophieGermainPrimeCandidate.multiply(TWO).add(BigInteger.ONE).isProbablePrime(100));
    return sophieGermainPrimeCandidate;
  }

  /**
   * Generates a random integer in [1, safePrime - 2]
   * @param safePrime
   * @return a random integer in [1, safePrime - 2]
   */
  private BigInteger getA(BigInteger safePrime){

    return null;
  }

}
