package com.chier.slave;

import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * @author lmdm
 * dataSource config './config/*.yaml' info ,check base info and format the object;
 */
public class BaseConfig implements Serializable {
    /**
     * hdfs path
     */
    private String WAREHOUSE_PATH = "";

    /**
     * hdfs compression type
     */
    private String HDFS_COMPRESSION_TYPE = "";

    /**
     * host url
     */
    private String SSH_HOST = "";
    /**
     * SSH name
     */
    private String SSH_NAME = "";

    /**
     * SSH PWD
     */
    private String SSH_PWD = "";

    /**
     * name node list
     */
    private String NAME_NODES = "";

    /**
     * node manager list
     */
    private String NODE_MANAGERS = "";

    /**
     * Ensures that the given object reference is not null. Upon violation, a NullPointerException with the given message is thrown.
     * reference – The object reference errorMessage – The message for the NullPointerException that is thrown if the check fails.
     * The object reference itself (generically typed).
     * NullPointerException – Thrown, if the passed reference was null.
     */
    public static <T> T checkNotNull(@Nullable T reference, @Nullable String errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    public BaseConfig(String path, String hostUrl, String host_ip, String name, String pwd, String nameNode, String nodeManager) {
        checkNotNull(path, "hdfs data warehouse path");
        checkNotNull(hostUrl, "host server url");
        checkNotNull(nameNode, "hadoop nameNode");
        checkNotNull(nodeManager, "hadoop nodeManager");
        this.WAREHOUSE_PATH = path;
        this.SSH_HOST = host_ip;
        this.SSH_NAME = name;
        this.SSH_PWD = pwd;
        this.NAME_NODES = nameNode;
        this.NODE_MANAGERS = nodeManager;
    }

    public String getHdfsPath() {
        return WAREHOUSE_PATH;
    }

    public String getCompressionType() {
        return HDFS_COMPRESSION_TYPE;
    }

    public String getNODE_MANAGERS() {
        return NODE_MANAGERS;
    }

    public String getNAME_NODES() {
        return NAME_NODES;
    }

    public String getSSH_PWD() {
        return SSH_PWD;
    }

    public String getSSH_NAME() {
        return SSH_NAME;
    }

    public String getSSH_HOST() {
        return SSH_HOST;
    }
}
