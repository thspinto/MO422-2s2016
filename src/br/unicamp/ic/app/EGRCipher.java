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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

public class EGRCipher extends CipherSpi {
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
    return 3007;
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

  protected void engineInit(int i, Key key, SecureRandom secureRandom) throws InvalidKeyException {
    if ((i == Cipher.DECRYPT_MODE)) {
      throw new InvalidParameterException("Decrypt must set Algorithm Parameters");
    }
    if (!(key instanceof EGRPublicKey)) {
      throw new InvalidKeyException("Encrypt requires public key");
    }
    this.key = key;
    this.random = secureRandom;
  }

  protected void engineInit(int i, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
    if (!(key instanceof EGRPublicKey)) {
      this.engineInit(i, key, secureRandom);
    } else {
      if (!(key instanceof EGRPrivateKey)) {
        throw new InvalidKeyException("Decrypt requires private key");
      }
      if (!(algorithmParameterSpec instanceof EGRParameterSpec)) {
        throw new InvalidParameterException("Invalid decrypt parameter");
      }
      this.algorithmParameterSpec = algorithmParameterSpec;
      this.key = key;
      this.random = secureRandom;
    }
  }

  protected void engineInit(int i, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
    throw new InvalidAlgorithmParameterException("Algorithm parameters not supported in this class");
  }

  protected byte[] engineUpdate(byte[] bytes, int i, int i1) {
    return engineDoFinal(bytes, i, i1);
  }

  protected int engineUpdate(byte[] bytes, int i, int i1, byte[] bytes1, int i2) throws ShortBufferException {
    return 0;
  }

  protected byte[] engineDoFinal(byte[] bytes, int i, int i1) {
    return new byte[0];
  }

  protected int engineDoFinal(byte[] bytes, int i, int i1, byte[] bytes1, int i2) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
    return 0;
  }

  private byte[] encrypt(byte[] bytes) {
    EGRPublicKey publicKey = (EGRPublicKey) key;
    BigInteger b = new BigInteger(256, random);
    this.dh = publicKey.getG().modPow(b, publicKey.getR());
    //Secret shared keys
    BigInteger k = publicKey.getGamma().modPow(b, publicKey.getR());
    BigInteger m = new BigInteger(bytes);

    //keep only 64 bit of k
    k = k.shiftRight(k.bitLength() - 64);
    //Concat k
    m.shiftLeft(64).or(k);

    // Encrypt
    BigInteger c = m.modPow(BigInteger.valueOf(2), publicKey.getN());

    return c.toByteArray();
  }

  private byte[] decrypt(byte[] bytes) {
    EGRPrivateKey privateKey = (EGRPrivateKey) key;
    EGRParameterSpec parameterSpec = (EGRParameterSpec) algorithmParameterSpec;
    //Getting shared key
    BigInteger k = parameterSpec.getDh().modPow(privateKey.getA(), privateKey.getR());
    BigInteger c = new BigInteger(bytes);
    BigInteger p = privateKey.getP();
    BigInteger q = privateKey.getQ();

    //Get the quadratic residues
    BigInteger m_p1 = c.modPow(q.add(BigInteger.ONE).divide(FOUR), p);
    BigInteger m_q1 = c.modPow(q.add(BigInteger.ONE).divide(FOUR), q);
    BigInteger m_p2 = p.subtract(m_p1);
    BigInteger m_q2 = q.subtract(m_q1);

    BigInteger x = privateKey.getX();
    BigInteger y = privateKey.getY();
    BigInteger N = privateKey.getN();

    BigInteger d1 = x.multiply(p).multiply(m_q1).add(y.multiply(q).multiply(m_p1)).mod(N);
    BigInteger d2 = x.multiply(p).multiply(m_q2).add(y.multiply(q).multiply(m_p1)).mod(N);
    BigInteger d3 = x.multiply(p).multiply(m_q1).add(y.multiply(q).multiply(m_p2)).mod(N);
    BigInteger d4 = x.multiply(p).multiply(m_q2).add(y.multiply(q).multiply(m_p2)).mod(N);

    //Find the correct message using the shared key

    return null;
  }
}
