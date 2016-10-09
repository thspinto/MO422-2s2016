package br.unicamp.ic.app;

import javax.crypto.*;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

public class EGRCipher extends CipherSpi {
  private Key key;
  private SecureRandom random;

  protected void engineSetMode(String s) throws NoSuchAlgorithmException {

  }

  protected void engineSetPadding(String s) throws NoSuchPaddingException {

  }

  protected int engineGetBlockSize() {
    return 0;
  }

  protected int engineGetOutputSize(int i) {
    return 0;
  }

  protected byte[] engineGetIV() {
    return new byte[0];
  }

  protected AlgorithmParameters engineGetParameters() {
    return null;
  }

  protected void engineInit(int i, Key key, SecureRandom secureRandom) throws InvalidKeyException {
    if ((i == Cipher.ENCRYPT_MODE) && !(key instanceof EGRPrivateKey)) {
      throw new InvalidKeyException("XOR requires an XOR key");
    }
    if ((i == Cipher.DECRYPT_MODE) && !(key instanceof EGRPublicKey)) {
      throw new InvalidKeyException("XOR requires an XOR key");
    }

    this.key = key;
    this.random = secureRandom;
  }

  protected void engineInit(int i, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
    throw new InvalidAlgorithmParameterException(
            "Algorithm parameters not supported in this class");
  }

  protected void engineInit(int i, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
    throw new InvalidAlgorithmParameterException(
            "Algorithm parameters not supported in this class");
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
}
