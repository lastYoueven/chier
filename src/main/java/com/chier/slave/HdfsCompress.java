package com.chier.slave;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @author Admin
 */
public class HdfsCompress extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 由于使用了SnappyCodec，Hadoop在读取文件时已经自动解压了数据  
        // 在这里你可以直接使用value，它已经是解压后的文本了  

        // 对value进行处理...  
        context.write(value, new Text("Processed: " + value.toString()));
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        // 设置Hadoop作业的配置（如果需要）  

        Job job = Job.getInstance(conf, "Snappy Text Processing");
        job.setJarByClass(HdfsCompress.class);

        // 设置输入和输出路径  
        Path inputPath = new Path("hdfs://your-hdfs-namenode:port/path/to/snappy/files/*");
        Path outputPath = new Path("hdfs://your-hdfs-namenode:port/path/to/output");

        // 设置输入格式和压缩编解码器  
        FileInputFormat.addInputPath(job, inputPath);
        FileInputFormat.setInputPaths(job, inputPath);
        job.setInputFormatClass(org.apache.hadoop.mapreduce.lib.input.TextInputFormat.class);
        // Hadoop会自动检测文件是否使用Snappy压缩，并使用相应的解码器  

        // 设置输出格式  
        FileOutputFormat.setOutputPath(job, outputPath);
        job.setOutputFormatClass(org.apache.hadoop.mapreduce.lib.output.TextOutputFormat.class);

        // 设置Mapper类  
        job.setMapperClass(HdfsCompress.class);

        // 设置Mapper的输出类型  
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        // 提交作业并等待完成  
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}