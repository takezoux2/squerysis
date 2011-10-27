package com.geishatokyo.squerysis.verify

import com.geishatokyo.squerysis.ExecutedSQL

/**
 * 
 * User: takeshita
 * Create: 11/10/27 1:22
 */

class ExplainResult(val sql : ExecutedSQL,  val explains : List[Explain]){
  override def toString = {
    """executed:%s
%s""".format(sql.sql,explains.mkString("\n"))
  }
}

class Explain(val id : Long,val selectType : SelectType.Value ,
              val table : String, val sweepType : SweepType.Value,
              val possibleKeys : List[String],val key : String,val keyLength : Int,
              val ref : String, val fetchRows : Int,val extra : String){
  override def toString = {
    """%s %s %s %s %s %s %s %s %s %s""".format(
      id,selectType,table,sweepType,possibleKeys.mkString(","),key,keyLength,ref,fetchRows,extra)
  }
}