package com.chier.slave;

import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class TaskOb {
    private final String JOB_PATH = System.getProperty("user.dir") + "/JobsConfig/";;
    private String TASK_COMM;
    private int PARALLELISM_NUM = 1;
    public TaskOb(String jobName){
        Yaml jb = new Yaml();
        Map<String, String> jconf = jb.load(JOB_PATH + jobName);
        this.TASK_COMM = jconf.get("command");
        this.PARALLELISM_NUM = Integer.parseInt(jconf.getOrDefault("command", "1"));

    }

}
