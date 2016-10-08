package br.unicamp.ic.app;

import java.math.BigInteger;
import java.security.PublicKey;

public class EGRPublicKey implements PublicKey {

  /**
   *  p and q are the two distinct primes p ≡ q ≡ 3 (mod 4)
   */
  private BigInteger p, q;

  /**
   * a is any integer in [1, r-2]
   * r is a random prime different from p and q.
   */
  private BigInteger a;

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
}

