package models.database

import doobie.implicits._
import models.{Input, Output, Transaction}

object TransactionsQueries {

  def getTransactionsByBlockId(blockId: String): doobie.ConnectionIO[List[Transaction]] = {
    val a = sql"SELECT * FROM transactions WHERE blockId = $blockId".query[Transaction].to[List]
    a
  }

  def getTransactionInputs(transactionId: String): doobie.ConnectionIO[List[Input]] =
    sql"SELECT * FROM inputs WHERE txId = $transactionId".query[Input].to[List]

  def getTransactionOutputs(transactionId: String): doobie.ConnectionIO[List[Output]] = {
    val a = sql"SELECT * FROM outputs WHERE txId = $transactionId".query[Output].to[List]
    println(a)
    a
  }
  def getTransactionById(transactionId: String): doobie.ConnectionIO[Option[Transaction]] =
    sql"SELECT * FROM transactions WHERE id = $transactionId".query[Transaction].to[List].map(_.headOption)
}