package com.geishatokyo.squerysis.verify

/**
 * 
 * User: takeshita
 * Create: 11/10/27 0:40
 */

trait VerifyResultReporter{

  def report( results : List[ExplainResult]) : Boolean

}