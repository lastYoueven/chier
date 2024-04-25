package com.chier.chierool;

import com.chier.slave.BaseConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lmdm
 */
public class GetConfig implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(GetConfig.class);
    private static File file;

    public static void setFile(File file) {
        GetConfig.file = file;
    }
    private static void initHdfsConfInfo(Map<String, Object> confInfo){
        Map<String, Object> sshData = (Map<String, Object>) confInfo.get("ssh_ssl");
        BaseConfig bc = new BaseConfig(sshData.get("hive.metastore.warehouse.dir").toString(),
                confInfo.get("hive_url").toString(),
                sshData.get("host").toString(),
                sshData.get("name").toString(),
                sshData.get("pwd").toString(),
                confInfo.get("nameNode").toString(),
                confInfo.get("nodeManager").toString());
    }

    private static void readYAMLConfig() {
        try {
            String confPath = System.getProperty("user.dir") + "/config/";
            File directory = new File(confPath);
            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".yaml"));
            if (files != null) {
                for (File file : files) {
                    LOG.info("get config files : " + file.getName());
                    InputStream inputStream = new FileInputStream(file);

                    Yaml yaml = new Yaml();
                    Map<String, Object> confInfo = yaml.load(inputStream);
                    if(file.getName().startsWith("hdfsConf")){
                        initHdfsConfInfo(confInfo);
                    }
                    inputStream.close();
                }
            } else {
                LOG.info("No YAML configuration files found in the directory path ./config");
            }
        } catch (FileNotFoundException e) {
            LOG.error("File not found: " + file.getName());
        } catch (Exception e) {
            LOG.error("Error reading YAML file: " + file.getName());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // test
        readYAMLConfig();
    }

}
