package com.saif

import com.typesafe.config.{Config, ConfigFactory}
import java.io.File
import scala.io.Source
import java.io._
import scala.collection.JavaConversions.mapAsScalaMap 
import scala.collection.mutable.{Map => MutableMap} 



object ConfigMngr {
  
  val config_file= "config/application.rtf"
  val parsedConfig = ConfigFactory.parseFile(new File("config/application.json"))
  val config = ConfigFactory.load(parsedConfig) 
  def getConfig() : Config = {
    config
  }

  val sourceFileDir = ConfigMngr.config.getString("appConfig.inputFilePath")
  val outputFileDir = ConfigMngr.config.getString("appConfig.outputFilePath")
  val sourceFileName = ConfigMngr.config.getString("appConfig.FileName")
  val recordTypes = ConfigMngr.config.getString("appConfig.RecordTypes")
  val fileSourceSystem = ConfigMngr.config.getString("appConfig.FileSourceSystem")
  val outputFilePrefix = ConfigMngr.config.getString("appConfig.outputFilePrefix")
  val numberOfPartitions = ConfigMngr.config.getString("appConfig.numOfPartitions").toInt
  val delimiter = ConfigMngr.getConfig().getString("appConfig.Delimiter")
  val recordTypeLength = ConfigMngr.config.getObject("recordTypeLength").toMap
  
  
}