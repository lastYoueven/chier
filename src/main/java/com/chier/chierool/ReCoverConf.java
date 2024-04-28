package com.chier.chierool;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.*;

class ReCoverConf {
    private static final String PRIVATE_KEY_FILE = System.getProperty("user.dir") + "/config/.tc.bar";
    private static final String PUBLIC_KEY_FILE = System.getProperty("user.dir") + "/config/.tp.bar";
    private static boolean isCover = false;

    public static void main(String[] args) {
        try {
            ReCoverConf();
            // 测试加解密
            String originalText = "Hello World!";
            byte[] encryptedText = encryptConf(originalText);
            String decryptedText = decryptConf(encryptedText);
            System.out.println("Original: " + originalText);
            System.out.println("Encrypted: " + new String(encryptedText));
            System.out.println("Decrypted: " + decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isEncryption(String encryptedBytes) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encryptedBytes.getBytes());
            keyFactory.generatePublic(publicKeySpec);
            return true;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return false;
        }
    }

    private static void ReCoverConf() {
        File privateKeyFile = new File(PRIVATE_KEY_FILE);
        File publicKeyFile = new File(PUBLIC_KEY_FILE);
        if (privateKeyFile.exists() && publicKeyFile.exists()) {
            isCover = true;
        } else {
            isCover = false;
        }
    }

    private static void generateKeys() throws NoSuchAlgorithmException, IOException, InterruptedException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        try (ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(PUBLIC_KEY_FILE));
             ObjectOutputStream privateKeyOS = new ObjectOutputStream(new FileOutputStream(PRIVATE_KEY_FILE))) {

            publicKeyOS.writeObject(keyPair.getPublic().getEncoded());
            privateKeyOS.writeObject(keyPair.getPrivate().getEncoded());
        }
        isCover = true;
    }

    private static PublicKey readPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE))) {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec((byte[]) inputStream.readObject());
            return keyFactory.generatePublic(publicKeySpec);
        } catch (ClassNotFoundException e) {
            throw new IOException("Error reading public key", e);
        }
    }

    private static PrivateKey readPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE))) {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec((byte[]) inputStream.readObject());
            return keyFactory.generatePrivate(privateKeySpec);
        } catch (ClassNotFoundException e) {
            throw new IOException("Error reading private key", e);
        }
    }

    public static byte[] encryptConf(String message) throws Exception {
        if (!isCover) {
            generateKeys();
        }
        PublicKey publicKey = readPublicKey();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(message.getBytes());
    }

    public static String decryptConf(byte[] encrypted) throws Exception {
        if (!isCover) {
            generateKeys();
        }
        PrivateKey privateKey = readPrivateKey();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encrypted);
        return new String(decryptedBytes);
    }
}
