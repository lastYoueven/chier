package com.chier.chierool;

import com.chier.slave.BaseConfig;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Base64;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author lmdm
 */
public class GetConfig extends ReCoverConf implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(GetConfig.class);
    private static BaseConfig HdfsConfig = null;

    static {
        try {
            String confPath = System.getProperty("user.dir") + "/config/";
            File directory = new File(confPath);
            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".yaml"));
            Yaml yaml = new Yaml();
            if (files != null) {
                for (File file : files) {
                    LOG.info("get config files : " + file.getName());
                    InputStream inputStream = new FileInputStream(file);
                    Map<String, Object> confInfo = yaml.load(inputStream);
                    if (file.getName().startsWith("hdfsConf")) {
                        initSshSsl(confInfo);
                    }
                    inputStream.close();
                }
            } else {
                LOG.info("No YAML configuration files found in the directory path ./config");
            }
        } catch (FileNotFoundException e) {
            LOG.error("File not found: " + e);
        } catch (Exception e) {
            LOG.error("Error reading YAML file: " + e);
            e.printStackTrace();
        }
    }

    /**
     * get the ssh/ssl info if encrypt to decrypt else cover and return
     *
     * @param key          base key name
     * @param base64String to check info
     * @return
     * @throws Exception
     */
    public static String updateSshSsl(String key, String base64String) throws Exception {
        try {
            return decryptConf(Base64.getDecoder().decode(base64String));
        } catch (Exception e) {
            reCoverConfig(key, encryptConf(base64String.getBytes()));
            return base64String;
        }
    }

    private static void initSshSsl(Map<String, Object> confInfo) throws Exception {
        Map<String, Object> sshData = (Map<String, Object>) confInfo.get("ssh_ssl");
        String host = sshData.get("host").toString();
        String name = sshData.get("name").toString();
        String pwd = sshData.get("pwd").toString();
        HdfsConfig = new BaseConfig(
                confInfo.get("hive.metastore.warehouse.dir").toString(),
                confInfo.get("hive_url").toString(),
                updateSshSsl("host", host),
                updateSshSsl("name", name),
                updateSshSsl("pwd", pwd),
                confInfo.get("nameNode").toString(),
                confInfo.get("nodeManager").toString());
    }

    private static void writeYamlFile(Map<String, Object> data, String filePath) {
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
            String yamlFilePath = System.getProperty("user.dir") + "/config/hdfsConf.yaml";
            FileReader reader = new FileReader(yamlFilePath);
            Yaml yaml = new Yaml();
            Map<String, Object> yamlConfig = yaml.load(reader);
            Map<String, Object> sshSslConfig = (Map<String, Object>) yamlConfig.get("ssh_ssl");
            sshSslConfig.put(keyName, Base64.getEncoder().encodeToString(encrypt));
            FileWriter writer = new FileWriter(yamlFilePath);
            yaml.dump(yamlConfig, writer);
            writeYamlFile(yamlConfig, yamlFilePath);
            reader.close();
            writer.close();
            LOG.info("YAML configuration file updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypt;
    }


    public static void main(String[] args) {
        // test
        GetConfig gc = new GetConfig();
    }

}
