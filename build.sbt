// Project name (artifact name in Maven)
name := "ParquetStreaming"

// Organization name (e.g. the package name of the project)
organization := "org.myorganization.spark.streaming"

// Version
version := "1.0"

// Project description
description := "Read Parquet files as streams"

// Do not append Scala versions to the generated artifacts
crossPaths := false

// Include Scala libraries into the dependency
autoScalaLibrary := true

// javac compiler options
javacOptions ++= Seq("-Xlint:unchecked")

// scalac compiler options
scalacOptions ++= Seq("-unchecked", "-deprecation")

// Compile first the Java code
compileOrder := CompileOrder.JavaThenScala

// Specify main class
mainClass in assembly := Some("org.myorganization.spark.streaming.ParquetStreaming")


lazy val commonSettings = Seq(
  scalaVersion := "2.10.6",
  libraryDependencies += "org.apache.spark"   % "spark-core_2.10"              % "1.6.1",
  libraryDependencies += "org.apache.spark"   % "spark-streaming_2.10"         % "1.6.1",
  libraryDependencies += "org.apache.spark"   % "spark-sql_2.10"               % "1.6.1",
  libraryDependencies += "org.apache.hadoop"  % "hadoop-common"                % "2.7.2",
  libraryDependencies += "org.apache.hadoop"  % "hadoop-mapreduce-client-core" % "2.7.2",
  libraryDependencies += "org.apache.parquet" % "parquet-hadoop"               % "1.8.1",
  libraryDependencies += "org.apache.parquet" % "parquet-common"               % "1.8.1",
  libraryDependencies += "org.apache.parquet" % "parquet-column"               % "1.8.1",
  libraryDependencies += "org.apache.parquet" % "parquet-format"               % "2.3.1",
  libraryDependencies += "org.apache.parquet" % "parquet-encoding"             % "1.8.1",
  libraryDependencies += "org.apache.parquet" % "parquet-avro"                 % "1.8.1",
  libraryDependencies += "org.apache.avro"    % "avro"                         % "1.8.0"
)


lazy val root = (project in file("."))
    .settings(
        commonSettings: _*
    )
    .settings(
        name := "ParquetStreaming",
        version := "1.0"
    )
