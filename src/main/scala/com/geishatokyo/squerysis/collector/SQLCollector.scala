package com.geishatokyo.squerysis.collector

import java.io.OutputStream
import java.util.concurrent.ConcurrentHashMap
import java.util.Date
import com.geishatokyo.squerysis.ExecutedSQL
import scala.collection.JavaConversions._

/**
 * 
 * User: takeshita
 * Create: 11/10/27 0:38
 */

trait SQLCollector{

  private val sqls = new ConcurrentHashMap[String,ExecutedSQL]()

  protected var recordSql = true

  private var reporters : List[SQLReporter] = Nil


  def addExecutedSql( sql : String , when : Date , elapsedMilliSec : Int) = {
    val executedSql = if(sqls.containsKey(sql)){
      sqls.get(sql)
    }else{
      val t = ExecutedSQL(sql)
      sqls.put(t.sql,t)
      t
    }
    executedSql.executedAt(when, elapsedMilliSec)
  }




  def start() = {
    recordSql = true
  }

  def stop() = {
    recordSql = false
  }

  def reset() = {
    recordSql = true
    sqls.clear()
  }

  def addReporter(reporter : SQLReporter) = {
    reporters = reporters :+ reporter
  }

  def removeReporter(reporter : SQLReporter) = {
    reporters = reporters.filter( _ != reporter)
  }

  def clearReporter() = reporters = Nil

  def executedSqls : List[ExecutedSQL] = sqls.values().toList

  def report() = {
    reporters.foreach(r => {
      r.report(executedSqls)
    })
  }

  def report(reporter : SQLReporter) = {
    reporter.report(executedSqls)
  }


}