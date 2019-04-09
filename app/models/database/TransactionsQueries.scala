package models.database

import doobie.free.connection.ConnectionIO
import doobie.implicits._
import models.{Directive, Input, Output, Transaction}

object TransactionsQueries {

  def getTransactionsByBlockId(blockId: String): doobie.ConnectionIO[List[Transaction]] =
    sql"SELECT * FROM transactions WHERE blockId = $blockId".query[Transaction].to[List]

  def getTransactionInputs(transactionId: String): doobie.ConnectionIO[List[Input]] =
    sql"SELECT * FROM inputs WHERE txId = $transactionId".query[Input].to[List]

  def getTransactionOutputs(transactionId: String): doobie.ConnectionIO[List[Output]] =
    sql"SELECT * FROM outputs WHERE txId = $transactionId".query[Output].to[List]

  def getTransactionDirectives(transactionId: String): doobie.ConnectionIO[List[Directive]] =
    sql"SELECT * FROM directives WHERE tx_id = $transactionId".query[Directive].to[List]

  def getTransactionById(transactionId: String): doobie.ConnectionIO[Option[Transaction]] =
    sql"SELECT * FROM transactions WHERE id = $transactionId".query[Transaction].to[List].map(_.headOption)

  def findTransactionQuery(id: String): ConnectionIO[Option[Transaction]] =
    sql"SELECT * FROM transactions WHERE id = $id".query[Transaction].to[List].map(_.headOption)

  def findTransactionByBlockHeightRangeQuery(from: Int, to: Int): ConnectionIO[List[Transaction]] =
    sql"SELECT * FROM transactions WHERE block_id IN (SELECT id FROM headers WHERE height BETWEEN $from AND $to)"
      .query[Transaction].to[List]
}