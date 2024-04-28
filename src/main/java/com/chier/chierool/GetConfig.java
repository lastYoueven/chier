package com.chier.chierool;

import com.chier.slave.BaseConfig;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.chier.chierool.ReCoverConf.*;


/**
 * @author lmdm
 */
public class GetConfig implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(GetConfig.class);
    private static File file;
    private static BaseConfig HdfsConfig = null;

    /**
     * init the config of hive
     */
    private static void GetConfig() {
        try {
            String confPath = System.getProperty("user.dir") + "/config/";
            File directory = new File(confPath);
            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".yaml"));
            if (files != null) {
                for (File file : files) {
                    System.out.println("get config files : " + file.getName());
                    InputStream inputStream = new FileInputStream(file);
                    Yaml yaml = new Yaml();
                    Map<String, Object> confInfo = yaml.load(inputStream);
                    if (file.getName().startsWith("hdfsConf")) {
                        initSshSsl(confInfo);
                    }
                    inputStream.close();
                }
            } else {
                System.out.println("No YAML configuration files found in the directory path ./config");
            }
        } catch (FileNotFoundException e) {
            LOG.error("File not found: " + file.getName());
        } catch (Exception e) {
            LOG.error("Error reading YAML file: " + file.getName());
            e.printStackTrace();
        }
    }

    public static String updateSshSsl(String key, String data) throws Exception {
        try {
            return decryptConf(data.getBytes());
        } catch (Exception e) {
            reCoverConfig(key, encryptConf(data));
            return data;
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
            System.out.println("YAML configuration written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String byteArrayToString(byte[] byteArray) {
        StringBuilder sb = new StringBuilder();
        for (byte b : byteArray) {
            sb.append(b).append(" ");
        }
        return sb.toString();
    }
    public static byte[] reCoverConfig(String keyName, byte[] config) {
        try {
            String yamlFilePath = System.getProperty("user.dir") + "/config/hdfsConf.yaml";
            FileReader reader = new FileReader(yamlFilePath);
            Yaml yaml = new Yaml();
            Map<String, Object> yamlConfig = yaml.load(reader);
            Map<String, Object> sshSslConfig = (Map<String, Object>) yamlConfig.get("ssh_ssl");
            sshSslConfig.put(keyName, byteArrayToString(config));
            FileWriter writer = new FileWriter(yamlFilePath);
            yaml.dump(yamlConfig, writer);
            writeYamlFile(yamlConfig, yamlFilePath);
            reader.close();
            writer.close();
            System.out.println("YAML configuration file updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    public static void main(String[] args) {
        // test
        GetConfig();
    }

}
