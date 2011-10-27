package com.geishatokyo.squerysis

import collector.impl.SquerylSQLCollector
import org.junit.runner.RunWith
import org.specs._
import org.specs.runner.{ JUnitSuiteRunner, JUnit }
import org.squeryl.sharding.builder.SimpleShardingSessionBuilder
import verify.impl.SquerylIndexVerifier
import org.squeryl.sharding.{ShardingSessionFactory, ShardingSession}
import org.squeryl.PrimitiveTypeMode
import org.squeryl.PrimitiveTypeMode._
import org.apache.log4j.BasicConfigurator
import verify.{IndexWarningLevel, SweepType, ExplainResult, VerifyResultReporter}
import java.util.Properties

@RunWith(classOf[JUnitSuiteRunner])
class AnalyzerTest extends Specification with JUnit{

  val ShardName = "default"

  lazy val collector = new SquerylSQLCollector()

  lazy val analyzer = new Analyzer(collector,new SquerylIndexVerifier(ShardName))

  doBeforeSpec{
    BasicConfigurator.configure
    val prop = new Properties()
    prop.load(getClass.getClassLoader.getResourceAsStream("db.properties"))
    val builder = new SimpleShardingSessionBuilder()
    builder.addWriter("jdbc:mysql://localhost/" + prop.getProperty("dbname"),
      prop.getProperty("username"),prop.getProperty("password"))
    builder.statisticsListener = Some(collector.getStatisticsListener _)
    builder.name = ShardName

    ShardingSessionFactory.addShard(builder.create())

    PrimitiveTypeMode.write(ShardName){
      Tables.drop
      Tables.create
    }

    analyzer.addReporter( new VerifyResultReporter{

      def report(results: List[ExplainResult]) = {
        for(er <- results){
          er.explains.foreach(e => {
            SweepType.level(e.sweepType) match{
              case IndexWarningLevel.Good => true
              case IndexWarningLevel.MaybeGood =>true
              case IndexWarningLevel.ShouldMakeIndex => {
                println( "should make index for next sql:" + er.sql.sql )
                true
              }
            }
          })
        }
        true
      }
    })

  }


  "squerysis" should{
    "is used as such" in{
      val table = Tables.analysisSample

      PrimitiveTypeMode.write(ShardName){
        table.insert(new AnalysisSample(0,12,34))
        table.insert(new AnalysisSample(0,43,29))
        table.insert(new AnalysisSample(0,4930,2039))

        table.where(t => t.id === 3).toList
        table.where(t => t.indexedNum === 12).toList
        table.where(t => t.notIndexed === 34).toList

        table.update(new AnalysisSample(2,32,29))

        update(table)( t => {
          where(t.id === 3221).set(t.indexedNum := 322)
        })

        table.deleteWhere(t => t.notIndexed === 422)
      }

      val results = analyzer.analyze()

      println(results.mkString("\n"))

    }
  }

}