package com.geishatokyo.squerysis.verify

import com.geishatokyo.squerysis._

/**
 * 
 * User: takeshita
 * Create: 11/10/27 0:40
 */

trait IndexVerifier{

  protected def mapToSelect( executed : ExecutedSQL) = {

    def toSelect = if(executed.extractAfterWhere.isDefined){
      SelectSQL("select * from " + executed.table.get + " " + executed.extractAfterWhere.get)
    }else{
      executed
    }

    val converted = executed match{
      case DoSQL(sql) => SelectSQL("select " + sql.substring(2))
      case DeleteSQL(sql) => toSelect
      case UpdateSQL(sql) => toSelect
      case ReplaceSQL(sql) => toSelect
      case _ => executed
    }
    (executed,converted)
  }

  protected def filterSelect(executed : ExecutedSQL) = {
    executed match{
      case SelectSQL(_) => true
      case _ => false
    }
  }

  protected def toExplainSql(sql : String) = {
    "explain " + SQLUtil.replacePlaceHolder(sql)
  }

  def verify( sqls : List[ExecutedSQL]) = {
    val explainSqls = sqls.map(mapToSelect(_)).withFilter( p => filterSelect(p._2)).map(p => {
      p._1 -> toExplainSql(p._2.sql)
    })
    val results =explainSqls.map(p => executeExplain(p._1,p._2))
    results
  }

  def executeExplain(executed : ExecutedSQL , explainSql : String) : ExplainResult





}