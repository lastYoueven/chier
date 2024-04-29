package com.chier.chierool;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class ReCoverConf extends RSAFun {
    private static boolean isCover = false;

    static {
        isReCoverConf();
    }

    public static void isReCoverConf() {
        File privateKeyFile = new File(PRIVATE_KEY_FILE);
        File publicKeyFile = new File(PUBLIC_KEY_FILE);
        if (privateKeyFile.exists() && publicKeyFile.exists()) {
            isCover = true;
        } else {
            isCover = false;
        }
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
    public byte[] callEncrypt(byte[] k) throws Exception {
        return encryptConf(k);
    }
    public String callDecrypt(byte[] k) throws Exception {
        return decryptConf(k);
    }
}
