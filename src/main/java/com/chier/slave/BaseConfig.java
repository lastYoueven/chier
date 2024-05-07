package com.chier.slave;

import org.yaml.snakeyaml.Yaml;

import javax.annotation.Nullable;
import java.io.*;
import java.util.Map;

import static com.chier.chierool.ReCoverConf.updateSshSsl;

/**
 * @author lmdm
 * dataSource config './config/*.yaml' info ,check base info and format the object;
 */
public class BaseConfig implements Serializable {
    /**
     * hdfs path
     */
    private String WAREHOUSE_PATH;
    /**
     * hdfs url
     */
    private String HDFS_URL;
    /**
     * hadoop url
     */
    private String HADOOP_URL;
    /**
     * hadoop name
     */
    private String HADOOP_NAME;
    /**
     * hadoop password
     */
    private String HADOOP_PWD;
    /**
     * OCEAN BASE url
     */
    private String OCEANBASE_URL;
    /**
     * OCEAN BASE name
     */
    private String OCEANBASE_NAME;
    /**
     * OCEAN BASE password
     */
    private String OCEANBASE_PWD;
    /**
     * mysql url
     */
    private String MYSQL_URL;
    /**
     * mysql name
     */
    private String MYSQL_NAME;
    /**
     * mysql password
     */
    private String MYSQL_PWD;
    /**
     * HIVE url
     */
    private String HIVE_URL;
    /**
     * data house path
     */
    private String DATA_HOUSE_PATH;
    /**
     * hdfs compression type
     */
    private String HDFS_COMPRESSION_TYPE;
    /**
     * host url
     */
    private String SSH_HOST;
    /**
     * SSH name
     */
    private String SSH_NAME;
    /**
     * SSH PWD
     */
    private String SSH_PWD;
    /**
     * name node list
     */
    private String NAME_NODE;
    /**
     * node manager list
     */
    private String NODE_MANAGERS;
    /**
     * the base config path
     */
    private final String CONF_FILE_PATH = System.getProperty("user.dir") + "/config/clusterConfig.yaml";
    /**
     * config data detail map of key/value
     */
    private final Map<String, Map<String, String>> Conf = loadConfigFile(this.CONF_FILE_PATH);

    /**
     * Ensures that the given object reference is not null. Upon violation, a NullPointerException with the given message is thrown.
     * reference – The object reference errorMessage – The message for the NullPointerException that is thrown if the check fails.
     * The object reference itself (generically typed).
     * NullPointerException – Thrown, if the passed reference was null.
     */
    private static <T> T checkNotNull(@Nullable T reference, @Nullable String errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    /**
     * Traverse a map of maps and replace any empty string values with an empty string.
     *
     * @param map The map of maps to process.
     */
    private static void replaceEmptyValues(Map<String, Map<String, String>> map) {
        // Iterate over the outer map
        for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
            // Get the inner map
            Map<String, String> innerMap = entry.getValue();

            // Iterate over the inner map
            for (Map.Entry<String, String> innerEntry : innerMap.entrySet()) {
                // Check if the value is empty
                if (innerEntry.getValue() == null || innerEntry.getValue().isEmpty()) {
                    // If empty, replace with an empty string
                    innerEntry.setValue("");
                }
            }
        }
    }

    private Map<String, Map<String, String>> loadConfigFile(String confName) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = new FileInputStream(confName)) {
            Map<String, Map<String, String>> cd = yaml.load(inputStream);
            replaceEmptyValues(cd);
            return cd;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initSSHSSL() throws Exception {
        Map<String, String> data = (Map<String, String>) Conf.get("ssh_ssl");
        this.SSH_HOST = updateSshSsl("host", checkNotNull(data.get("host"), "Ssh/Ssl ip is null"));
        this.SSH_NAME = updateSshSsl("name", checkNotNull(data.get("name"), "Ssh/Ssl name is null"));
        this.SSH_PWD = updateSshSsl("pwd", checkNotNull(data.get("pwd"), "Ssh/Ssl pwd is null"));
    }

    public void initHdfsConf() {
        Map<String, String> hdfsData = (Map<String, String>) Conf.get("hadoop");
        this.WAREHOUSE_PATH = checkNotNull(hdfsData.get("hive.metastore.warehouse.dir"), "hadoop path is null");
        this.HADOOP_URL = checkNotNull(hdfsData.get("url"), "url  is null");
        this.NAME_NODE = checkNotNull(hdfsData.get("nameNode"), "hadoop nameNode is null");
        this.NODE_MANAGERS = checkNotNull(hdfsData.get("nodeManager"), "hadoop nodeManager is null");
        this.HADOOP_NAME = hdfsData.get("name");
        this.HADOOP_PWD = hdfsData.get("pwd");
        this.HIVE_URL = hdfsData.get("hive_url");
        this.HDFS_URL = hdfsData.get("url");
        this.DATA_HOUSE_PATH = hdfsData.get("hive.metastore.warehouse.dir");
    }

    public void initOBConf() {
        Map<String, String> data = (Map<String, String>) Conf.get("oceanbase");
        this.OCEANBASE_URL = checkNotNull(data.get("url"), "OceanBase connection url is null");
        this.OCEANBASE_NAME = checkNotNull(data.get("name"), "OceanBase name is null");
        this.OCEANBASE_PWD = checkNotNull(data.get("pwd"), "OceanBase pwd is null");
    }

    public void initMysqlConf() {
        Map<String, String> data = (Map<String, String>) Conf.get("oceanbase");
        this.MYSQL_URL = checkNotNull(data.get("url"), "OceanBase connection url is null");
        this.MYSQL_NAME = checkNotNull(data.get("name"), "OceanBase name is null");
        this.MYSQL_PWD = checkNotNull(data.get("pwd"), "OceanBase pwd is null");
    }

    public String getHdfsPath() {
        return WAREHOUSE_PATH;
    }

    public String getCompressionType() {
        return HDFS_COMPRESSION_TYPE;
    }

    public String getNodeManagers() {
        return NODE_MANAGERS;
    }

    public String getNameNodes() {
        return NAME_NODE;
    }

    public String getSshPwd() {
        return SSH_PWD;
    }

    public String getSshName() {
        return SSH_NAME;
    }

    public String getSshHost() {
        return SSH_HOST;
    }

    public String getOceanbaseUrl() {
        return OCEANBASE_URL;
    }

    public void setOceanbaseUrl(String url) {
        this.OCEANBASE_URL = url;
    }

    public String getHadoopPwd() {
        return HADOOP_PWD;
    }

    public void setHadoopPwd(String pwd) {
        this.HADOOP_PWD = pwd;
    }

    public String getHdfsUrl() {
        return HDFS_URL;
    }

    public void setHdfsUrl(String url) {
        this.HDFS_URL = url;
    }

    public String getHadoopUrl() {
        return HADOOP_URL;
    }

    public void setHadoopUrl(String url) {
        this.HADOOP_URL = url;
    }

    public String getHadoopName() {
        return HADOOP_NAME;
    }

    public void setHadoopName(String name) {
        this.HADOOP_NAME = name;
    }

    public String getOceanbaseName() {
        return OCEANBASE_NAME;
    }

    public void setOceanbaseName(String name) {
        this.OCEANBASE_NAME = name;
    }

    public String getOceanbasePwd() {
        return OCEANBASE_PWD;
    }

    public void setOceanbasePwd(String pwd) {
        this.OCEANBASE_PWD = pwd;
    }

    public String getHiveUrl() {
        return HIVE_URL;
    }

    public void setHiveUrl(String url) {
        this.HIVE_URL = url;
    }

    public String getDataHousePath() {
        return DATA_HOUSE_PATH;
    }

    public void setDataHousePath(String path) {
        this.DATA_HOUSE_PATH = path;
    }

    public String getMysqlUrl() {
        return MYSQL_URL;
    }

    public void setMysqlUrl(String url) {
        this.MYSQL_URL = url;
    }

    public String getMysqlName() {
        return MYSQL_NAME;
    }

    public void setMysqlName(String name) {
        this.MYSQL_NAME = name;
    }

    public String getMysqlPwd() {
        return MYSQL_PWD;
    }

    public void setMysqlPwd(String pwd) {
        this.MYSQL_PWD = pwd;
    }
    public String getConfPath() {
        return this.CONF_FILE_PATH;
    }
    public Map<String,Map<String,String>> getConf(){
        return this.Conf;
    }
}
