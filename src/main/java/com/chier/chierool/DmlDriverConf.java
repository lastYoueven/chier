package com.chier.chierool;

public class DmlDriverConf {
    /**
     * data source connection driver
     */
    private Object SourceConn = null;
    /**
     * data sink connection driver
     */
    private Object SinkConn = null;
    private Object connPool = null;
    private String SQL_PATH = System.getProperty("user.dir") + "/sqlJobs";

    private int connNum = 0;
    private void getConnectionConfig(){

    }
    private void initMysqlConnection(){

    }
    private void initOBConnection(){

    }

}
