package com.geishatokyo.squerysis.collector.impl

import com.geishatokyo.squerysis.collector.SQLCollector
import org.squeryl.logging.{StatementInvocationEvent, StatisticsListener}
import java.util.Date

/**
 * 
 * User: takeshita
 * Create: 11/10/27 11:48
 */

class SquerylSQLCollector extends SQLCollector{
  object collector extends StatisticsListener{
    def deleteExecuted(se: StatementInvocationEvent) = add(se)

    def insertExecuted(se: StatementInvocationEvent) = add(se)

    def updateExecuted(se: StatementInvocationEvent) = add(se)

    def resultSetIterationEnded(statementInvocationId: String, iterationEndTime: Long, rowCount: Int, iterationCompleted: Boolean) {}

    def queryExecuted(se: StatementInvocationEvent) = add(se)

    def add(se: StatementInvocationEvent) : Unit = {
      if(recordSql)  addExecutedSql(se.jdbcStatement,new Date(se.start),(se.end - se.start).toInt)
    }
  }

  def getStatisticsListener() : StatisticsListener = collector
}