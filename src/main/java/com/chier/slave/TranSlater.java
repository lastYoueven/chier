package com.chier.slave;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * @author lmdm
 * translate the sql to map reduce work stream
 */
public class TranSlater extends BaseConfig {

    public void getHdfsFile() throws IOException, URISyntaxException {
        this.initHdfsConf();
        Configuration conf = new Configuration();
        // 设置HDFS相关的配置（如果需要）

        // 2. 打开文件系统
        FileSystem fs = FileSystem.get(new URI(this.getHdfsUrl()), conf);

        // 3. 打开文件
        Path filePath = new Path("/home/000000_0.snappy");
        FSDataInputStream inputStream = fs.open(filePath);

        // 创建一个缓冲区来读取数据
        byte[] buffer = new byte[1024];
        byte[] decompressedData = Snappy.uncompress(buffer);

        // 5. 处理数据（这里只是简单打印）
        System.out.println(new String(decompressedData, "UTF-8"));

        // 6. 关闭资源
        inputStream.close();
        fs.close();
    }

}
