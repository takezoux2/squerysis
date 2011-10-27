package com.geishatokyo.squerysis.verify

/**
 * 
 * User: takeshita
 * Create: 11/10/27 1:23
 */

object SelectType extends Enumeration{

  val Simple = Value("SIMPLE")
  val Primary = Value("PRIMARY")
  val Union = Value("UNION")
  val DependentUnion = Value("DEPENDENT UNION")
  val UnionResult = Value("UNION RESULT")
  val SubQuery = Value("SUBQUERY")
  val DependentSubQuery = Value("DEPENDENT SUBQUERY")
  val Derived = Value("DERIVED")
  val UncacheableSubQuery = Value("UNCACHEABLE SUBQUERY")

  val Unknown = Value("UNKNOWN")

  def safeWithName(name : String) = {
    try{
      withName(name)
    }catch{
      case e : Exception => {
        println("Unkown name : " + name)
        Unknown
      }
    }
  }

}

object SweepType extends Enumeration{



  val System = Value("system")
  val Const = Value("const")
  val EqRef = Value("eq_ref")
  val Ref = Value("ref")
  val RefOrNull = Value("refOrNull")
  val IndexMerge = Value("index_merge")
  val UniqueSubQuery = Value("unique_subquery")
  val IndexSubQuery = Value("index_subquery")
  val Range = Value("range")
  val Index = Value("index")
  val All = Value("ALL")

  val Unknown = Value("Uuknown")

  def level( sweepType : SweepType.Value) = {
    sweepType match{
      case System | Const | EqRef => IndexWarningLevel.Good
      case Ref | RefOrNull | Range | IndexMerge=> IndexWarningLevel.MaybeGood
      case Index | All => IndexWarningLevel.ShouldMakeIndex
    }
  }
  def safeWithName(name : String) = {
    try{
      withName(name)
    }catch{
      case e : Exception => {
        println("Unkown name : " + name)
        Unknown
      }
    }
  }

}

object IndexWarningLevel extends Enumeration{
  val Good = Value
  val MaybeGood = Value
  val ShouldMakeIndex = Value
}