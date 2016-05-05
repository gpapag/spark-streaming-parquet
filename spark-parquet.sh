#!/bin/bash
#
# Submits the scala parquetstreaming script to Apache Spark

export LD_LIBRARY_PATH=${HADOOP_HOME}/lib/native:${LD_LIBRARY_PATH}

IVY="${HOME}/.ivy2/cache"
JARS=("org.apache.hadoop/hadoop-common/jars/hadoop-common-2.7.2.jar"                               \
      "org.apache.hadoop/hadoop-mapreduce-client-core/jars/hadoop-mapreduce-client-core-2.7.2.jar" \
      "org.apache.parquet/parquet-hadoop/jars/parquet-hadoop-1.8.1.jar"                            \
      "org.apache.parquet/parquet-common/jars/parquet-common-1.8.1.jar"                            \
      "org.apache.parquet/parquet-column/jars/parquet-column-1.8.1.jar"                            \
      "org.apache.parquet/parquet-format/jars/parquet-format-2.3.1.jar"                            \
      "org.apache.parquet/parquet-encoding/jars/parquet-encoding-1.8.1.jar"                        \
      "org.apache.parquet/parquet-avro/jars/parquet-avro-1.8.1.jar"                                \
      "org.apache.avro/avro/jars/avro-1.7.7.jar")

JARSLIB=""
for jar in ${JARS[@]}; do
    if [[ "${JARSLIB}" != "" ]]; then
        JARSLIB+=","
    fi
    JARSLIB+=${IVY}/$jar
done


MASTER="yarn"
DMODE="client"

spark-submit --master "${MASTER}" --deploy-mode "${DMODE}" --jars "${JARSLIB}" "$@"
