package com.chier.chierool;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class test {
    public static void getDataFromHive() {
        // 创建 SparkSession
        SparkConf conf = new SparkConf()
                .setAppName("ReadFromHive")
                .setMaster("local"); // 本地模式运行

        // 创建 SparkSession
        SparkSession spark = SparkSession.builder()
                .config(conf)
                .enableHiveSupport() // 启用对 Hive 的支持
                .getOrCreate();

        // 读取 Hive 中的 dw_enterprise_base_info 表
        Dataset<Row> dwEnterpriseBaseInfo = spark.table("dw_enterprise_base_info");

        // 取前 1000 条数据
        Dataset<Row> first1000Rows = dwEnterpriseBaseInfo.limit(1000);

        // 显示结果
        first1000Rows.show();

        // 关闭 SparkSession
        spark.stop();
    }
}
