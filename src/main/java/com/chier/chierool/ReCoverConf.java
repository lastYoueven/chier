package com.chier.chierool;

import java.io.*;

public class ReCoverConf extends RSAFun {
    private static boolean isCover = false;

    static {
        isReCoverConf();
    }

    /**
     * check encrypt pub/pri key
     */
    public static void isReCoverConf() {
        File privateKeyFile = new File(PRIVATE_KEY_FILE);
        File publicKeyFile = new File(PUBLIC_KEY_FILE);
        if (privateKeyFile.exists() && publicKeyFile.exists()) {
            isCover = true;
        } else {
            isCover = false;
        }
    }

    public byte[] callEncrypt(byte[] k) throws Exception {
        return encryptConf(k);
    }
    public String callDecrypt(byte[] k) throws Exception {
        return decryptConf(k);
    }
}
