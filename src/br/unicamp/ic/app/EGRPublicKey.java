package br.unicamp.ic.app;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.PublicKey;

public class EGRPublicKey implements PublicKey, Serializable {
  /**
   * N p * q (from the private key)
   */
  private BigInteger N;

  /**
   * r is a random prime different from p and q (from the private key).
   */
  private BigInteger r;

  /**
   * g is a primitive root for Fp
   */
  private BigInteger g;

  /**
   * Gamma is g^a mod r, where a is a random integer in [1, r-2]
   */
  private BigInteger gamma;

  public String getAlgorithm() {
    return Properties.name;
  }

  public String getFormat() {
    return "";
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

  public BigInteger getG() {
    return g;
  }

  public void setG(BigInteger g) {
    this.g = g;
  }

  public BigInteger getGamma() {
    return gamma;
  }

  public void setGamma(BigInteger gamma) {
    this.gamma = gamma;
  }
}
