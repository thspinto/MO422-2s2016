package br.unicamp.ic.app;

import java.math.BigInteger;

public class EGRCipher {

  /**
   * Ciphered message.
   */
  private BigInteger c;

  /**
   * g^b for the Diffie-Hellman key exchange.
   */

  private BigInteger delta;

  public BigInteger getC() {
    return c;
  }

  public void setC(BigInteger c) {
    this.c = c;
  }

  public BigInteger getDelta() {
    return delta;
  }

  public void setDelta(BigInteger delta) {
    this.delta = delta;
  }
}
