package org.example.chierool;

import org.apache.spark.sql.SparkSession;


public class test {
    public static void getDataFromHive() {
        // 创建 SparkSession
        SparkSession spark = SparkSession.builder()
                .appName("TestSpark")
                .enableHiveSupport() // 启用 Hive 支持
                .getOrCreate();

        // 获取 Hive 中的数据库列表
        spark.sql("SHOW DATABASES").show();

        // 停止 SparkSession
        spark.stop();
    }
}
