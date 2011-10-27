package com.geishatokyo.squerysis.collector.impl

import com.geishatokyo.squerysis.collector.{SQLReportLoader, SQLReporter}
import com.geishatokyo.squerysis.ExecutedSQL
import java.text.SimpleDateFormat
import java.io.{FileReader, BufferedReader, FileInputStream, FileOutputStream}

/**
 * 
 * User: takeshita
 * Create: 11/10/27 12:16
 */

class CSVReporter(filename : String,append : Boolean = true) extends SQLReporter with SQLReportLoader{


  def dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  def report(sqls: List[ExecutedSQL]) = {
    val output = new FileOutputStream(filename,append)
    try{
      def write( cells : Any*) = {
        output.write(( cells.mkString(",") + "\n").getBytes("utf-8"))
      }
      val format = dateFormat
      sqls.foreach( s => {
        write("SQL",s.sql)
        for( t <- s.executeTimes){
          write(format.format(t._1),t._2)
        }
      })
    }finally{
      if(output != null){
        output.close()
      }
    }
    true
  }

  def loadReport() = {

    val reader = new BufferedReader(new FileReader(filename))

    var line = reader.readLine
    var current : ExecutedSQL = null
    var sqls : List[ExecutedSQL] = Nil
    val format = dateFormat
    while(line != null){
      if(line.startsWith("#") || line.startsWith("//")){
        // skip line
      }else{
         val cols = line.split(",")
        cols(0) match{
          case "SQL" => {
            if(current != null){
              sqls = sqls :+ current
            }
            current = ExecutedSQL(cols(1))
          }
          case _ => {
            current.executedAt(format.parse(cols(0)),cols(1).toInt)
          }
        }

      }
    }
    if(current != null){
      sqls = sqls :+ current
    }
    sqls
  }
}