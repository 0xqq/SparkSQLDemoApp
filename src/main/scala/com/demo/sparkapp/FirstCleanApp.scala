package com.demo.sparkapp

import com.demo.Utils.DateUtil
import org.apache.spark.sql.SparkSession

/**
  * 1.从原始数据中截取信息
  * 访问时间、访问URL、耗费的流量、访问IP地址信息
  */
object FirstCleanApp {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("FirstCleanApp").master("local[2]").getOrCreate()
    val access = spark.sparkContext.textFile("D:\\access.test.log")

    //开始处理
    access.map(line => {
      val splits = line.split(" ")
      val ip = splits(0)
      val time = splits(3) + " " + splits(4)

      val url = splits(11).replaceAll("\"", "")
      val traffic = splits(9)

      DateUtil.parse(time) + "\t" + url + "\t" + traffic + "\t" + ip

    }).saveAsTextFile("D:\\clean_first.log")//.take(30).foreach(println)

    spark.stop()
  }

}