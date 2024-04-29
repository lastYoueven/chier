package com.chier.chierool;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;

public class RSAFun {
    static final String PRIVATE_KEY_FILE = System.getProperty("user.dir") + "/config/.tc.bk";
    static final String PUBLIC_KEY_FILE = System.getProperty("user.dir") + "/config/.tp.bk";
    static PublicKey publicKey = null;
    static PrivateKey privateKey = null;

    /**
     * initial public key & private key
     */
    static {
        try (ObjectInputStream publicKeyIS = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
             ObjectInputStream privateKeyIS = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE))) {
            publicKey = (PublicKey) publicKeyIS.readObject();
            privateKey = (PrivateKey) privateKeyIS.readObject();
        } catch (Exception e) {
            try {
                generateKeys();
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * gen the publicKey & privateKey
     *
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws InterruptedException
     */
    private static void generateKeys() throws NoSuchAlgorithmException, IOException, InterruptedException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        try (ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(PUBLIC_KEY_FILE));
             ObjectOutputStream privateKeyOS = new ObjectOutputStream(new FileOutputStream(PRIVATE_KEY_FILE))) {

            publicKeyOS.writeObject(keyPair.getPublic());
            privateKeyOS.writeObject(keyPair.getPrivate());
        }
    }

    /**
     * rsa 1024 Encrypt
     * @param message to encrypt
     * @return encrypt data
     * @throws Exception
     */
    public static byte[] encryptConf(byte[] message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(message);
    }

    /**
     * rsa 1024 decrypt
     * @param encrypted
     * @return decrypt data
     * @throws Exception
     */
    public static String decryptConf(byte[] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encrypted);
        return new String(decryptedBytes);
    }
}
