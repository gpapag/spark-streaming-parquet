package org.myorganization.spark.streaming


import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{StreamingContext, Seconds, Time}
import org.apache.spark.sql.SQLContext
import org.apache.hadoop.fs.Path
import org.apache.parquet.hadoop.ParquetInputFormat
import org.apache.parquet.avro.AvroReadSupport
import org.apache.avro.generic.GenericRecord


/** 
 * Read in Parquet files as a Stream and create DataFrames to contain
 * the data.
 */
object ParquetStreaming {
    def main(args: Array[String]): Unit = {
        val (batchDuration, inputDir) = getParams(args)

        val conf = new SparkConf().setAppName("gpParquetStreaming")
        val sc   = new SparkContext(conf)
        val ssc  = new StreamingContext(sc, Seconds(batchDuration))

        ssc.sparkContext.hadoopConfiguration.set("parquet.read.support.class", "org.apache.parquet.avro.AvroReadSupport")

        val stream = ssc.fileStream[Void, GenericRecord, ParquetInputFormat[GenericRecord]](inputDir, { path: Path => path.toString.endsWith("parquet") }, true, ssc.sparkContext.hadoopConfiguration)

        val dataStream = stream.transform(rdd => rdd.map(tuple => tuple._2)).persist
        val countData = dataStream.count

        dataStream.transform(rdd => rdd.map(record => record.toString)).foreachRDD((rdd: RDD[String], time: Time) => {
            val sqlContext = SQLContextSingleton.getInstance(rdd.sparkContext)
            import sqlContext.implicits._

            val dataDF = sqlContext.read.json(rdd)
            dataDF.printSchema
            dataDF.show
        })

        countData.print
        dataStream.print
        stream.print

        ssc.start()
        ssc.awaitTermination()
    }


    def getParams(args: Array[String]): Tuple2[Int, String] = {
        if (args.length != 2) {
            System.err.println(s"""
                |Usage: spark-parquet.sh <sampling-period> <parquet-dir>
                |  <sampling-period>  the duration of each batch (in seconds)
                |  <parquet-dir>      the directory where new parquet files are added
                |
                """.stripMargin)
            System.exit(1)
        }
        Tuple2[Int, String](args(0).toInt, args(1))
    }
}


/**
 * Lazily instantiated singleton instance of SQLContext.
 */
object SQLContextSingleton {
    @transient private var instance: SQLContext = _

    def getInstance(sparkContext: SparkContext): SQLContext = {
        if (instance == null) {
            instance = new SQLContext(sparkContext)
        }
        instance
    }
}
