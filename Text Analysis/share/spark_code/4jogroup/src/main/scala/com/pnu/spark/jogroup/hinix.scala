package com.pnu.spark.jogroup


import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Dataset
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
//import scala.collection.immutable.Map
import org.spark_project.guava.io.BaseEncoding
//import org.apache.spark.sql.catalyst.expressions.Base64

object hacker {
   
  //val stock_code = "015760","005380"
  def main(args: Array[String]): Unit = {
  val conf=new SparkConf()
  .setMaster("local[*]")
  .set("spark.shuffle.service.enabled", "true")
  .set("spark.dynamicAllocation.enabled", "true")
  .set("spark.shuffle.service.enabled", "true")
  .set("spark.io.compression.codec", "snappy")
  .set("spark.rdd.compress", "true")
  // .setJars()
   .setAppName("pivot")
  val sc=new SparkContext(conf)    
  val sqlContext = new org.apache.spark.sql.SQLContext(sc)
  import sqlContext.implicits._    
     val spark = SparkSession.builder()
    /* .config("spark.driver.memory","5g")
     .config("spark.executor.memory", "4g")*/
     .appName("HDP Test Job")
    .master("local[*]")
    .config("spark.driver.cores",4)
    .config("spark.driver.memory","24g")
       //.config("spark.debug.maxToStringFields",2000000)       
    .config("spark.hadoop.fs.defaultFS", "hdfs://master:9000")
    //.config("spark.yarn.jars", "hdfs://master:9000/spark/jars/*.zip")
    .config("spark.hadoop.yarn.resourcemanager.address", "master:8032")
    .config("spark.hadoop.yarn.application.classpath", 
       "$HADOOP_CONF_DIR,$HADOOP_COMMON_HOME/*,$HADOOP_COMMON_HOME/lib/*,$HADOOP_HDFS_HOME/*,$HADOOP_HDFS_HOME/lib/*,$HADOOP_MAPRED_HOME/*,$HADOOP_MAPRED_HOME/lib/*,$HADOOP_YARN_HOME/*,$HADOOP_YARN_HOME/lib/*")
     .config("spark.sql.pivotMaxValues",300000)  
/*     .config("spark.driver.memory","5g")
     .config("spark.executor.memory", "4g")
    .enableHiveSupport()*/
.getOrCreate()
     


 val inputPath="hdfs://master:9000/kai_hacker/"
 val outputPath="hdfs://master:9000/output_hacker/"
 //val df=spark.read.text(inputPath)

  
 val df=spark.read.parquet(inputPath+"insta_팬스타크루즈_kai.parquet")
 //val df=spark.read.option("header","true").csv(inputPath)
 ///print(df.show(10))
 val text = df.select(lower(df("text")))
 val date = df.select(df("date"))
 //print("비어잇는지 아닌지 검증") 
 //print(text.filter(v => v == null).collect().mkString("\n"))
 val text_rdd = text.as(Encoders.STRING).rdd
    
 //print(text_rdd.collect().mkString(","))
 val date_rdd = date.as(Encoders.STRING).rdd

 
 
 
 
 
 val date_text= date_rdd.zip(text_rdd).flatMapValues(_.split(" ")).toDF().withColumnRenamed("_1", "date").withColumnRenamed("_2", "text").groupBy("date").pivot("text").agg(count("date")).na.fill(0).coalesce(1).write.parquet("insta_팬스타크루즈_kai.parquet")

 


 /*
 val split_rdd = date_text.flatMapValues(_.split(" "))
 
 val date_text_DS=split_rdd.toDF()
 
 val date_text_DS_cols=date_text_DS.withColumnRenamed("_1", "date").withColumnRenamed("_2", "text")
  

 
 print("pivot start") 
 val pivot_DS = date_text_DS_cols.groupBy("date").pivot("text").agg(count("date")).na.fill(0)//
 print("pivot end")
 print("write write")
 pivot_DS.coalesce(1).write.parquet(outputPath+"naver_"+stock_code_list(i)+"_freq.parquet")
*/
 print("끝났다 끝났어!!")
  
  //pivot_DS.coalesce(1).write.format("com.databricks.spark.csv").option("header",true).save(outputPath)
 
  
 //.pivot("text").agg(count("date")).na.fill(0).write.format("com.databricks.spark.csv").option("header",true).save(outputPath) 
 //val tmp_df = df.toDF("date", "pos")
 //print(tmp_df.show(10))
 
 //val indice=df.as(Encoders.STRING).rdd
 //print(indice.collect().mkString(","))

// print(indice.toDF().show(10))
// val indice_rev=indice.map(_.swap)
 //val indice_DS=df.toDS()


//val indice_draft=df.rdd.flatMapValues(_.split(" "))
//val indice_refine=indice_draft.filter(_._2.contains("NNP")).filter(_._2.length()>1)
// val indice_DST=indice_refine.toDS()
// println(indice_DST.groupBy($"_1").pivot("_2").agg(count("_1")).na.fill(0).show(10))
// println(indice_DST.groupBy("_1").pivot("_2").agg(count("_1")).show(10))
 
 // println("na",indice_DS.na)
    
 // indice_DST.groupBy("_1").pivot("_2").agg(count("_1")).write.format("com.databricks.spark.csv").option("header",true).save(outputPath) 
     }
}