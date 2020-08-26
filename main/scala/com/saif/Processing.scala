package com.saif

import scala.collection.JavaConversions.mapAsScalaMap 
import scala.collection.mutable.{Map => MutableMap}
import scala.io.Source


object Processing {
  
  
  val func = new utils()
  val global_recordSchema = loadSchema()
  val errorlogfile = "output/error_log"
  
  
  def loadSchema () : MutableMap [String, MutableMap[Int, MutableMap[String, String]]] = {
    
    var recordTypeMap = MutableMap[String, MutableMap[Int,MutableMap[String,String ]]]()
    for (recordType <- ConfigMngr.recordTypes.split(",")) {
      var subParentMap = MutableMap[Int, MutableMap[String, String]]()
      val configFile="config/recordType_"+recordType
      val bufferedSource = Source.fromFile(configFile)
      for (line <- bufferedSource.getLines) {
        val cols = line.split('|').map(_.trim)
        var childMap = MutableMap("startpos" -> cols(1).mkString,"endpos" -> cols(2).mkString,"columnName" -> cols(3).mkString,"dataType" -> cols(4).mkString, "decimalpos" -> cols(5) )
        subParentMap += (cols(0).toInt -> childMap)
      }
    recordTypeMap += (recordType -> subParentMap)
    bufferedSource.close()
  }
  return recordTypeMap
  }
  

  
  
  def processRow(row:String): String = {
    var result = ""
    try{
      
      val rowRecordType = func.getRecordType(row) 
      if ((ConfigMngr.recordTypes contains rowRecordType) && func.isRecordLengthValid(row,rowRecordType) ) {
        if (ConfigMngr.recordTypes contains rowRecordType)  {
        val recordTypeCoulmnSequence=global_recordSchema(rowRecordType)
        val columnArray = new Array[String] (recordTypeCoulmnSequence.keys.max.toInt)
        for (cols <- recordTypeCoulmnSequence.keys) {
          val recordTypeCoulmnDetails= recordTypeCoulmnSequence(cols)        
          val startpos = recordTypeCoulmnDetails("startpos").toInt -1
          val endpos = recordTypeCoulmnDetails("endpos").toInt
          val decimalpos = recordTypeCoulmnDetails("decimalpos").toInt
          val colDataType = recordTypeCoulmnDetails("dataType").toLowerCase()
          val columnIndex=(cols.toInt)-1
         // val nextcolumn = formatColumn(row.substring(startpos,endpos).trim(),colDataType)
          val nextcolumn = row.substring(startpos,startpos + endpos).trim()
          columnArray(columnIndex)=nextcolumn
         
        }     
        result= (columnArray.mkString(ConfigMngr.delimiter))
      }
    }
    }
    catch{
      case e: Exception => func.writeToFile(errorlogfile,e+"\n" + row)
      return("")
    }
     return result
  }
  
  

  
}