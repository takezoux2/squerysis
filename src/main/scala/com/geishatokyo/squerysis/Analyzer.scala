package com.geishatokyo.squerysis

import collector.SQLCollector
import verify.{ExplainResult, VerifyResultReporter, IndexVerifier}

/**
 * 
 * User: takeshita
 * Create: 11/10/27 12:33
 */

class Analyzer(sqlCollector : SQLCollector,verifier : IndexVerifier){
  def start() = sqlCollector.start()
  def stop() = sqlCollector.stop()
  def reset() = sqlCollector.reset()
  def analyze() = {
    sqlCollector.report()
    val explainResults = verifier.verify(sqlCollector.executedSqls)
    reportResult(explainResults)
    explainResults
  }

  def addReporter(reporter : VerifyResultReporter) = {
    reporters = reporters :+ reporter
  }

  private var reporters : List[VerifyResultReporter] = Nil

  def reportResult( results : List[ExplainResult]) = {
    reporters.foreach(r => {
      r.report(results)
    })
  }

}