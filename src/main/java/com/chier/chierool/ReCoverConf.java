package com.chier.chierool;

import com.chier.slave.BaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Base64;
import java.util.Map;

/**
 * @author lmdm
 */
public class ReCoverConf extends RSAFun {
    private static final Logger LOG = LoggerFactory.getLogger(ReCoverConf.class);
    private static boolean isCover = false;
    private static final BaseConfig bc = new BaseConfig();

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
    /**
     * get the ssh/ssl info if encrypt to decrypt else cover and return
     *
     * @param key          base key name
     * @param base64String to check info
     * @return String
     * @throws Exception decode ERROR
     */
    public static String updateSshSsl(String key, String base64String) throws Exception {
        try {
            return decryptConf(Base64.getDecoder().decode(base64String));
        } catch (Exception e) {
            reCoverConfig(key, encryptConf(base64String.getBytes()));
            return base64String;
        }
    }
    private static void writeYamlFile(Map<String, Map<String, String>> data, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            yaml.dump(data, writer);
            LOG.info("YAML configuration written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * cover the SSH/SSL config
     *
     * @param keyName SSH/SSl key name
     * @param encrypt encrypt data
     * @return
     */
    public static byte[] reCoverConfig(String keyName, byte[] encrypt) {
        try {
            Map<String, String> sshSslConfig = bc.getConf().get("ssh_ssl");
            Yaml yaml = new Yaml();
            sshSslConfig.put(keyName, Base64.getEncoder().encodeToString(encrypt));
            FileWriter writer = new FileWriter(bc.getConfPath());
            yaml.dump(sshSslConfig, writer);
            writeYamlFile(bc.getConf(), bc.getConfPath());
            writer.close();
            LOG.info("YAML configuration file updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypt;
    }
}
