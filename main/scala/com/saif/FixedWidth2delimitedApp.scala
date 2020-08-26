package com.saif

import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark._
import java.io.File
import scala.io.Source
import java.io._
import org.apache.spark.rdd.RDD
import scala.util.Properties
import scala.collection._
import scala.collection.convert.decorateAsScala._
import scala.collection.JavaConversions.mapAsScalaMap 
import scala.collection.mutable.{Map => MutableMap}
import com.typesafe.config.{Config, ConfigFactory}
object FixedWidth2delimitedApp   {
 
  
    def main (args:Array[String]) {
    
     val spark = SparkSession
      .builder
      .appName("SparkSCD")      
      .master("local[2]")
      .getOrCreate()
      System.setProperty("hadoop.home.dir", "/Users/saifniazi/workspace/software/hadoop-common-2.2.0-bin-master")

      
    val sourceFileAbsPath=ConfigMngr.sourceFileDir+ConfigMngr.sourceFileName
    val sourceFileRdd = spark.read.textFile(sourceFileAbsPath).rdd
    val fileRdd = sourceFileRdd.map{row => Processing.processRow(row)}
    fileRdd.foreach(println)
//    createOutputFile(fileRdd)
//    System.in.read()
    spark.stop()
  }
  
  
 
  
}