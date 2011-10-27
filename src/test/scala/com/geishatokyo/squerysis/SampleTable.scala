package com.geishatokyo.squerysis

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.{KeyedEntity, PrimitiveTypeMode, Schema}

/**
 * 
 * User: takeshita
 * Create: 11/10/27 14:39
 */

class AnalysisSample(var id : Long , var indexedNum : Int , var notIndexed : Int) extends KeyedEntity[Long]{
  def this() = this(0,0,0)
}


object Tables extends Schema{

  val analysisSample = table[AnalysisSample]

  on(analysisSample)(t => declare(
    t.indexedNum is(indexed)
  ))

}