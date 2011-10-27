package com.geishatokyo.squerysis

import java.util.Date
import collection.immutable.HashSet

/**
 * 
 * User: takeshita
 * Create: 11/10/27 0:52
 */

abstract class ExecutedSQL( _sql : String){

  val sql = _sql.replace("\r","").replace("\n"," ")

  private var _executeTimes : List[(Date,Int)] = Nil
  /**
   * Add executed time
   * @param when executed time
   * @param elapsedMilliSec elapsed time to finish sql
   */
  def executedAt( when : Date, elapsedMilliSec : Int) = {
    sql.synchronized{
      _executeTimes = _executeTimes :+(when -> elapsedMilliSec)
    }
  }

  def executeTimes = _executeTimes

  private var afterWhere : Option[Option[String]] = None
  def extractAfterWhere : Option[String] = {
    if(!afterWhere.isDefined){
      val i = sql.toLowerCase.indexOf("where")
      if(i >= 0){
        afterWhere = Some(Some(sql.substring(i)))
      }else{
        afterWhere = Some(None)
      }
    }
    afterWhere.get
  }
  protected var _table : Option[Option[String]] = None
  def table : Option[String] = {
    if(!_table.isDefined){
      _table = Some(_extractTable())
    }
    _table.get
  }

  protected def _extractTable() : Option[String] = {
    val split = sql.toLowerCase.split(" ")
    val index = split.indexOf("from")
    if(index >= 0){
      Some(split(index + 1))
    }else{
      None
    }
  }

}

object ExecutedSQL{
  def apply(sql : String) : ExecutedSQL = {
    sql.substring(0,10.min(sql.length)).trim().split(" ")(0).toLowerCase match{
      case "select" => SelectSQL(sql)
      case "delete" => DeleteSQL(sql)
      case "truncate" => DeleteSQL(sql)
      case "insert" => InsertSQL(sql)
      case "do" => DoSQL(sql)
      case "update" => UpdateSQL(sql)
      case "replace" => ReplaceSQL(sql)
      case "drop" => DropSQL(sql)
      case _ => OtherSQL(sql)
    }
  }
}

case class SelectSQL(_sql : String) extends ExecutedSQL(_sql)
case class DeleteSQL(_sql : String) extends ExecutedSQL(_sql)
case class UpdateSQL(_sql : String) extends ExecutedSQL(_sql){
  val Preserved = HashSet("update","low_priority","ignore")
  override def _extractTable() = {
    sql.split(" ").find(!Preserved.contains(_))
  }
}
case class InsertSQL(_sql : String) extends ExecutedSQL(_sql){

  val Preserved = HashSet("insert","low_priority","delayed","high_priority","ignore","into")

  override def _extractTable() = {
    sql.toLowerCase.split(" ").find( p => {
      !Preserved.contains(p)
    })
  }
}
case class ReplaceSQL(_sql : String) extends ExecutedSQL(_sql){

  val Preserved = HashSet("replace","low_priority","delayed","high_priority","ignore","into")

  override def _extractTable() = {
    sql.toLowerCase.split(" ").find( p => {
      !Preserved.contains(p)
    })
  }
}
case class DoSQL(_sql : String) extends ExecutedSQL(_sql)
case class DropSQL(_sql : String) extends ExecutedSQL(_sql)
case class OtherSQL(_sql : String) extends ExecutedSQL(_sql)