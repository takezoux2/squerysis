package com.geishatokyo.squerysis.collector

import com.geishatokyo.squerysis.ExecutedSQL

/**
 * 
 * User: takeshita
 * Create: 11/10/27 0:51
 */

trait SQLReporter{


  def report( sqls : List[ExecutedSQL]) : Boolean

}

trait SQLReportLoader{
  def loadReport() : List[ExecutedSQL]
}