package models.database

import doobie.implicits._
import models.{Contract, Input, Output, Transaction}

object TransactionsQueries {

  def getTransactionsByBlockId(blockId: String): doobie.ConnectionIO[List[Transaction]] =
    sql"SELECT * FROM transactions WHERE blockId = $blockId".query[Transaction].to[List]

  def getTransactionInputs(transactionId: String): doobie.ConnectionIO[List[Input]] =
    sql"SELECT * FROM inputs WHERE txId = $transactionId".query[Input].to[List]

  def getTransactionOutputs(transactionId: String): doobie.ConnectionIO[List[Output]] =
    sql"SELECT * FROM outputs WHERE txId = $transactionId".query[Output].to[List]

  def getTransactionById(transactionId: String): doobie.ConnectionIO[Option[Transaction]] =
    sql"SELECT * FROM transactions WHERE id = $transactionId".query[Transaction].to[List].map(_.headOption)

  def getOutput(id: String): doobie.ConnectionIO[Option[Output]] =
    sql"SELECT * FROM outputs WHERE id = $id".query[Output].to[List].map(_.headOption)

  def getContract(transactionId: String): doobie.ConnectionIO[List[Contract]] =
    sql"select * from contracts where hash IN (select contract from inputs where txId = $transactionId)".query[Contract].to[List]
}