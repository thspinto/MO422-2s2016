package br.unicamp.ic.app;

import com.google.gson.Gson;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Random;

import javax.crypto.Cipher;

import static java.lang.System.exit;

public class Main {
  private static Random random = new SecureRandom();
  private static BigInteger TWO = BigInteger.valueOf(2);
  private static HelpFormatter helpFormatter = new HelpFormatter();

  public static void main(String args[]) throws Exception {
    Options options =
        new Options().addOption("m", true, "Mode [E encrypt | D decrypt | G generate keys]").
            addOption("kdir", true, "Keys directory");

    final CommandLineParser lineParser = new DefaultParser();

    CommandLine commandLine = lineParser.parse(options, args);
    if (!(commandLine.hasOption("m") && commandLine.hasOption("kdir"))) {
      printHelp(options);
      exit(0);
    }

    String keysDir = commandLine.getOptionValue("kdir");
    ModeEnum mode = ModeEnum.getByCode(commandLine.getOptionValue("m"));
    if (mode == null) {
      printHelp(options);
      exit(0);
    }

    if (ModeEnum.GENERATE.equals(mode)) {
      generateKeyPair(keysDir);
      exit(0);
    }

    EGREngine c = new EGREngine();

    String input = readInput();
    Gson gson = new Gson();
    if (ModeEnum.ENCRYPT.equals(mode)) {
      if (input.getBytes().length > 376) {
        System.err.println("Message size must be less then 376 bytes");
        exit(0);
      }
      EGRPublicKey publicKey = readKey(keysDir + Properties.publicKeyFileName, EGRPublicKey.class);
      c.engineInit(Cipher.ENCRYPT_MODE, publicKey, new SecureRandom());
      EGRCipher cipher = new EGRCipher();
      cipher.setC(new BigInteger(c.encrypt(input.getBytes())));
      cipher.setDelta(new BigInteger(c.engineGetIV()));
      gson.toJson(cipher, System.out);
    } else {
      EGRCipher cipher = gson.fromJson(input, EGRCipher.class);
      EGRPrivateKey privateKey =
          readKey(keysDir + Properties.privateKeyFileName, EGRPrivateKey.class);
      EGRParameterSpec paramSpec = new EGRParameterSpec();
      paramSpec.setDh(cipher.getDelta());
      c.engineInit(Cipher.DECRYPT_MODE, privateKey, paramSpec, new SecureRandom());
      System.out.println(new String(c.decrypt(cipher.getC().toByteArray())));
    }
    exit(0);
  }

  private static String readInput() throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringBuilder input = new StringBuilder();
    String line = null;
    while ((line = br.readLine()) != null) {
      input.append(line + "\n");
    }
    return input.toString();
  }

  private static void generateKeyPair(String keysDir) throws IOException, NoSuchAlgorithmException {
    Provider egrProvider = new EGRProvider();
    Security.addProvider(egrProvider);
    KeyPairGenerator kpg;
    kpg = KeyPairGenerator.getInstance(Properties.name);
    kpg.initialize(Properties.bitLength, new SecureRandom());
    KeyPair keyPair = kpg.generateKeyPair();

    saveKeysToFile(keysDir, keyPair);
  }

  private static void saveKeysToFile(String keysDir, KeyPair keyPair) throws IOException {
    //Create dir if doesn't exist
    File file = new File(keysDir);
    file.mkdir();

    Gson gson = new Gson();
    FileWriter out = new FileWriter(keysDir + "/EGRPrivateKey");
    gson.toJson(keyPair.getPrivate(), out);
    out.close();

    out = new FileWriter(keysDir + "/EGRPublicKey");
    gson.toJson(keyPair.getPublic(), out);
    out.close();
  }

  private static <T> T readKey(String filePath, Class<T> keyClass) throws FileNotFoundException {
    Gson gson = new Gson();

    return gson.fromJson(new FileReader(filePath), keyClass);
  }

  private static void printHelp(Options options) {
    helpFormatter.printHelp("java -jar EGamalRabin.jar <options> <message>", options);

    System.out.println("Examples:\n"

        + "java -jar ElGamalRabin.jar -kdir key_dir -m G \n"

        + "java -jar EGamalRabin.jar -kdir key_dir -m E < message.txt \n"

        + "java -jar EGamalRabin.jar -kdir key_dir -m D < cipher.txt");
  }

  private static BigInteger[] encrypt(BigInteger m, KeyPair keyPair) {

    return null;
  }

  private static BigInteger decrypt(BigInteger c, KeyPair keyPair) {

    return null;
  }

  private enum ModeEnum {
    GENERATE("G"), ENCRYPT("E"), DECRYPT("D");

    private String code;

    ModeEnum(String code) {
      this.code = code;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public static ModeEnum getByCode(String code) {
      for (ModeEnum e : ModeEnum.values()) {
        if (e.getCode().equals(code)) {
          return e;
        }
      }
      return null;
    }
  }
}

