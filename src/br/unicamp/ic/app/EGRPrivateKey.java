package br.unicamp.ic.app;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.PrivateKey;

public class EGRPrivateKey implements PrivateKey, Serializable {

  /**
   *  p and q are the two distinct primes p ≡ q ≡ 3 (mod 4)
   */
  private BigInteger p, q;

  /**
   * a is any integer in [1, r-2]
   * r is a random prime different from p and q.
   */
  private BigInteger a;

  /**
   * r is a random prime different from p and q (from the private key).
   */
  private BigInteger r;

  /**
   * g is a primitive root for Fp
   */
  private BigInteger g;

  public String getAlgorithm() {
    return "EGR";
  }

  public String getFormat() {
    return "ElGamal Rabin Algorithm";
  }

  public byte[] getEncoded() {
    return new byte[0];
  }

  public BigInteger getP() {
    return p;
  }

  public void setP(BigInteger p) {
    this.p = p;
  }

  public BigInteger getQ() {
    return q;
  }

  public void setQ(BigInteger q) {
    this.q = q;
  }

  public BigInteger getA() {
    return a;
  }

  public void setA(BigInteger a) {
    this.a = a;
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
}

