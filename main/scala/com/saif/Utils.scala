package com.saif

import org.apache.spark.rdd.RDD

class utils {

  /*  Knowing whihc row belongs to which recordType or table*/
  def getRecordType(row: String): String = {
    val recordType = ConfigMngr.fileSourceSystem match {
      case "TET" => row(0).toString
    }
    return recordType
  }

  
  /* Checking length of each row according to our expectation */
  def isRecordLengthValid(row: String, recordType: String): Boolean = {
    val expectedrecordLength = ConfigMngr.recordTypeLength("recordType_" + recordType).render.toInt

    if (row.length() != expectedrecordLength) {
      val eermsg = "Record Length Validation Error: Record Type -> " + recordType + "Expected Record Length -> " + expectedrecordLength +
        "Acutal record length: " + row.length().toString + "Row ->" + row
      println(eermsg)
      return false
    } else {
      return true
    }
  }

 /* Writing Output file on external path */ 
  def createOutputFile(rdd: RDD[String]): Unit = {
    for (recordType <- ConfigMngr.recordTypes.split(",")) {
      val path = ConfigMngr.outputFileDir + ConfigMngr.outputFilePrefix + recordType
      val data = rdd.filter(line => line.startsWith(recordType))
      data.coalesce(1, true).saveAsTextFile(path)
    }
  }

 /* Putting decimal value if required */ 
  def formatColumn(column: String, colDataType: String): String = {
    var value = ""

    if (colDataType.startsWith("x")) {
      value = column
    } else if (colDataType.startsWith("9")) {
      if (colDataType.contains("v")) {
        val r = """.*v\((.*)\).*""".r //regular expression to extract decimal point number
        val decimalPoint = colDataType match {
          case r(value) => value
          case _        => "0"
        }
        value = ((if (column == "") "0" else column).toDouble / scala.math.pow(10, decimalPoint.toInt)).toString
      } else {
        value = column
      }
    }
    return value.trim()
  }

 /* Writing log on local file */ 
  def writeToFile(canonicalFilename: String, text: String) {
    //  scala.tools.nsc.ip.path(canonicalFilename) .createFile().appendAll(text+"\n")
  }

}