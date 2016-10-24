package br.unicamp.ic.app;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

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
    privateKey.setN(publicKey.getN());

    //Calculate the extended GDC parameters
    BigInteger[] ext = extended_gcd(privateKey.getP(), privateKey.getQ());
    privateKey.setX(ext[1]);
    privateKey.setY(ext[2]);

    //Diffie-Hellman key exchange (Using ElGamal's idea)
    KeyPairGenerator kpg = null;
    try {
      kpg = KeyPairGenerator.getInstance("DH");
      kpg.initialize(512, random);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    //Generate diffie-hellman keys
    KeyPair dhkp = kpg.generateKeyPair();
    DHParameterSpec dhParams = ((DHPrivateKey) dhkp.getPrivate()).getParams();

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
   * Generates a random prime such that p ≡ 3 (mod 4).
   *
   * @return a random blum prime
   */
  private BigInteger getBlumPrime() {
    BigInteger blumPrimeCandidate;
    do {
      blumPrimeCandidate = BigInteger.probablePrime(bitLength / 2, random);
    } while (!blumPrimeCandidate.mod(FOUR).equals(THREE));
    return blumPrimeCandidate;
  }

  /**
   * Calculates the extended GDC of a and b.
   *
   * @param a any BigInteger
   * @param b any BigInteger
   * @return BigInteger vector {d, x, y} such that ax + by = d
   */
  public static BigInteger[] extended_gcd(BigInteger a, BigInteger b) {
    BigInteger x1 = BigInteger.ZERO;
    BigInteger x2 = BigInteger.ONE;
    BigInteger y1 = BigInteger.ONE;
    BigInteger y2 = BigInteger.ZERO;
    while (!b.equals(BigInteger.ZERO)) {
      BigInteger[] qr = a.divideAndRemainder(b);
      BigInteger q = qr[0];

      BigInteger x = x2.subtract(q.multiply(x1));
      BigInteger y = y2.subtract(q.multiply(y1));

      a = b;
      b = qr[1];
      x2 = x1;
      x1 = x;
      y2 = y1;
      y1 = y;
    }
    return new BigInteger[]{a, x2, y2};
  }
}
