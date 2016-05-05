# spark-streaming-parquet
Scala code to read Parquet files as streams in Spark Streaming using Avro.

## Build
The `build.sbt` file sets various configuration parameters and lists the dependencies of the code to other components. 
To compile the code:
```
sbt clean compile
```
To create the jar file that is submitted to Spark:
```
sbt package
```
This will create `target/parquetstreaming-1.0.jar`.

## Submit to Spark
The Bash script `spark-parquet.sh` can be used to submit the code to Spark:
```
spark-parquet.sh target/parquetstreaming-1.0.jar <sampling-period (seconds)>  <parquet-dir>
```
The script collects the jar files that the Scala code depends on and submits them to Spark.

## Output
The `parquet-dir` is checked every `sampling-period` seconds for a new Parquet file (the file extension should be _.parquet_).
The stream is converted to a DataFrame. The schema of the DataFrame is printed along with the top 20 rows of data. Moreover,
the total number of rows in the Parquet file is printed followed by the data first and then the complete record of 
(key, data) for each row in the file.
