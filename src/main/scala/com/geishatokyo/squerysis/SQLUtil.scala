package com.geishatokyo.squerysis

/**
 * 
 * User: takeshita
 * Create: 11/10/27 0:45
 */

object SQLUtil{

  def replacePlaceHolder(sql : String) = {
    sql.replace("?","\"1\"")
  }

}