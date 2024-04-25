#!/usr/bin/env bash

# check Hadoop env
if [ -z "$HADOOP_HOME" ] || [ ! -x "$HADOOP_HOME/bin/hadoop" ]; then
    echo "Hadoop environment not found. Make sure HADOOP_HOME is set correctly."
    exit 1
fi

if [ -z "$HIVE_HOME" ] || [ ! -x "$HIVE_HOME/bin/hive" ]; then
    echo "Hive environment not found. Make sure HIVE_HOME is set correctly."
    exit 1
fi
# get dataWarehouse
$HIVE_HOME/bin/hive -e 'SET hive.metastore.warehouse.dir;'| awk 'END { print }'| sed 's/=/: /g' >> $(pwd)/config/.venvConf.yaml