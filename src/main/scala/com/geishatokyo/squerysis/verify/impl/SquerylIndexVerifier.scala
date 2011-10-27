package com.geishatokyo.squerysis.verify.impl

import org.squeryl.PrimitiveTypeMode
import org.squeryl.sql.ResultSetProcessor
import java.sql.ResultSet
import com.geishatokyo.squerysis.ExecutedSQL
import com.geishatokyo.squerysis.verify._
import org.slf4j.LoggerFactory

/**
 * 
 * User: takeshita
 * Create: 11/10/27 11:52
 */

class SquerylIndexVerifier(shardName : String) extends IndexVerifier{

  val logger = LoggerFactory.getLogger(classOf[SquerylIndexVerifier])

  var ignoreError = false

  def executeExplain(executed: ExecutedSQL, explainSql: String) = {
    import PrimitiveTypeMode._
    logger.debug("Exec explain : " + explainSql)
    val explains = execute(shardName)( dao => {
      dao.execQuery(explainSql + ";" , new ResultSetProcessor[Explain]{
        def eachResult(resultSet: ResultSet) = {

          var index = 0
          def counter = {
            index += 1
            index
          }

          def selectType( v : String) = {
            try{

            }
          }

          try{
            val e = new Explain(
            resultSet.getLong(counter),
            SelectType.safeWithName(resultSet.getString(counter)),
            resultSet.getString(counter),
            SweepType.safeWithName(resultSet.getString(counter)),
            {
              val keys = resultSet.getString(counter)
              if(keys != null){
                keys.split(",").toList
              }else{
                Nil
              }
            },
            resultSet.getString(counter),
            resultSet.getInt(counter),
            resultSet.getString(counter),
            resultSet.getInt(counter),
            resultSet.getString(counter)
            )

            Some(e)
          }catch{
            case e : Exception => {
              e.printStackTrace()
              if(ignoreError){
                None
              }else{
                throw e
              }
            }
          }
        }
      })
    })

    new ExplainResult(executed,explains)

  }
}