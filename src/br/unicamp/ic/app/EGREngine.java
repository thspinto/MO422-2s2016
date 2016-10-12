package br.unicamp.ic.app;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

/**
 * I was going to use Java SDK's standard architecture for encryption, but Oracle mandates me to get
 * a certificate for it (US encryption export policy).
 *
 * Error: "Caused by: java.lang.SecurityException: Cannot verify jar:file:/Users/thiago/workspace/RabinElgamal/build/classes/production/ElGamalRabin/!/"
 */
public class EGREngine extends CipherSpi {
  boolean encrypt = true;
  private Key key;
  private SecureRandom random;
  private BigInteger dh;
  private AlgorithmParameterSpec algorithmParameterSpec;
  private static BigInteger FOUR = BigInteger.valueOf(4);

  protected void engineSetMode(String s) throws NoSuchAlgorithmException {
    throw new NoSuchAlgorithmException("Unsupported mode " + s);
  }

  protected void engineSetPadding(String s) throws NoSuchPaddingException {
    throw new NoSuchPaddingException("Unsupported padding " + s);
  }

  protected int engineGetBlockSize() {
    return 64;
  }

  protected int engineGetOutputSize(int i) {
    return i;
  }

  protected byte[] engineGetIV() {
    return dh.toByteArray();
  }

  protected AlgorithmParameters engineGetParameters() {
    return null;
  }

  public void engineInit(int i, Key key, SecureRandom secureRandom) throws InvalidKeyException {
    if ((i == Cipher.DECRYPT_MODE)) {
      throw new InvalidParameterException("Decrypt must set Algorithm Parameters");
    }
    if (!(key instanceof EGRPublicKey)) {
      throw new InvalidKeyException("Encrypt requires public key");
    }
    this.key = key;
    this.random = secureRandom;
  }

  public void engineInit(int i, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
    if (!(key instanceof EGRPrivateKey)) {
      throw new InvalidKeyException("Decrypt requires private key");
    }
    if (!(algorithmParameterSpec instanceof EGRParameterSpec)) {
      throw new InvalidParameterException("Invalid decrypt parameter");
    }
    this.encrypt = false;
    this.algorithmParameterSpec = algorithmParameterSpec;
    this.key = key;
    this.random = secureRandom;
  }

  protected void engineInit(int i, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
    throw new InvalidAlgorithmParameterException("Algorithm parameters not supported in this class");
  }

  protected byte[] engineUpdate(byte[] bytes, int i, int i1) {
    return new byte[0];
  }

  protected int engineUpdate(byte[] bytes, int i, int i1, byte[] bytes1, int i2) throws ShortBufferException {
    return 0;
  }

  protected byte[] engineDoFinal(byte[] bytes, int i, int i1) throws IllegalBlockSizeException, BadPaddingException {
    return new byte[0];
  }

  protected int engineDoFinal(byte[] bytes, int i, int i1, byte[] bytes1, int i2) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
    return 0;
  }

  private byte[] engineDoFinal(byte[] bytes) {
    if (encrypt) {
      return encrypt(bytes);
    }
    return decrypt(bytes);
  }

  public byte[] encrypt(byte[] bytes) {
    EGRPublicKey publicKey = (EGRPublicKey) key;
    BigInteger b = new BigInteger(256, random);
    this.dh = publicKey.getG().modPow(b, publicKey.getR());
    //Secret shared keys
    BigInteger k = publicKey.getGamma().modPow(b, publicKey.getR());
    BigInteger m = new BigInteger(bytes);

    //keep only 64 bit of k
    k = k.shiftRight(k.bitLength() - 64);
    //Concat k
    m = m.shiftLeft(64).or(k);

    // Encrypt
    BigInteger c = m.modPow(BigInteger.valueOf(2), publicKey.getN());

    return c.toByteArray();
  }

  public byte[] decrypt(byte[] bytes) {
    EGRPrivateKey privateKey = (EGRPrivateKey) key;
    BigInteger c = new BigInteger(bytes);
    BigInteger p = privateKey.getP();
    BigInteger q = privateKey.getQ();

    //Get the quadratic residues
    BigInteger r = c.modPow(p.add(BigInteger.ONE).divide(FOUR), p);
    BigInteger s = c.modPow(q.add(BigInteger.ONE).divide(FOUR), q);

    BigInteger x = privateKey.getX();
    BigInteger y = privateKey.getY();
    BigInteger N = privateKey.getN();

    BigInteger xps = x.multiply(p).mod(N).multiply(s).mod(N);
    BigInteger yqr = y.multiply(q).mod(N).multiply(r).mod(N);

    BigInteger m1 = xps.add(yqr).mod(N);
    BigInteger m2 = xps.subtract(yqr).mod(N);

    List<BigInteger> dArray = Arrays.asList(m1, m1.negate().mod(N), m2, m2.negate().mod(N));

    //Find the correct message using the shared key
    EGRParameterSpec parameterSpec = (EGRParameterSpec) algorithmParameterSpec;
    //Getting shared key
    BigInteger k = parameterSpec.getDh().modPow(privateKey.getA(), privateKey.getR());
    k = k.shiftRight(k.bitLength() - 64);

    for (BigInteger d : dArray) {
      if (d.xor(k).setBit(64).getLowestSetBit() == 64) {
        return d.shiftRight(64).toByteArray();
      }
    }

    return null;
  }
}
