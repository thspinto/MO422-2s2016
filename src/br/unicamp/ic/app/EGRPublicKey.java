package br.unicamp.ic.app;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;

public class EGRPrivateKey implements PrivateKey, Serializable {
  /**
   * N p * q (from the private key)
   */
  private BigInteger N;

  /**
   * r is a random prime different from p and q (from the private key).
   */
  private BigInteger r;

  /**
   * Alpha is a primitive root for Fp
   */
  private BigInteger alpha;


  /**
   * Gamma is alpha^a mod r, where a is a random integer in [1, r-2]
   */
  private BigInteger gamma;

  public String getAlgorithm() {
    return "EGR";
  }

  public String getFormat() {
    return "ElGamal Rabin Algorithm";
  }

  public byte[] getEncoded() {
    return new byte[0];
  }

  public BigInteger getN() {
    return N;
  }

  public void setN(BigInteger n) {
    N = n;
  }

  public BigInteger getR() {
    return r;
  }

  public void setR(BigInteger r) {
    this.r = r;
  }

  public BigInteger getAlpha() {
    return alpha;
  }

  public void setAlpha(BigInteger alpha) {
    this.alpha = alpha;
  }

  public BigInteger getGamma() {
    return gamma;
  }

  public void setGamma(BigInteger gamma) {
    this.gamma = gamma;
  }
}
