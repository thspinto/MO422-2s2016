package br.unicamp.ic.app;

import java.security.Provider;

public class EGRProvider extends Provider {

  /**
   * Constructs a provider with the specified name, version number,
   * and information.
   */
  protected EGRProvider() {
    super(Properties.name, Properties.version, "ElGamal Rabin Security Provider v0");
    put("Cipher.EGR", "br.unicamp.ic.app.EGREngine");
    put("KeyPairGenerator.EGR", "br.unicamp.ic.app.EGRKeyPairGenerator");
  }
}
