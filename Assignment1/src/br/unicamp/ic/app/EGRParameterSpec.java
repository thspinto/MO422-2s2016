package br.unicamp.ic.app;

import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;

public class EGRParameterSpec implements AlgorithmParameterSpec {
  /**
   * Diffie-Helman key exange. Message specific key.
   */
  private BigInteger dh;

  public BigInteger getDh() {
    return dh;
  }

  public void setDh(BigInteger dh) {
    this.dh = dh;
  }
}
