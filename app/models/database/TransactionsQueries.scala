package models.database

import doobie.implicits._
import models.{Contract, DBInput, DBOutput, DBTransaction}

object TransactionsQueries {

  def getTransactionsByBlockId(blockId: String): doobie.ConnectionIO[List[DBTransaction]] =
    sql"SELECT * FROM transactions WHERE blockId = $blockId".query[DBTransaction].to[List]

  def getTransactionInputs(transactionId: String): doobie.ConnectionIO[List[DBInput]] =
    sql"SELECT * FROM inputs WHERE txId = $transactionId".query[DBInput].to[List]

  def getTransactionOutputs(transactionId: String): doobie.ConnectionIO[List[DBOutput]] =
    sql"SELECT * FROM outputs WHERE txId = $transactionId".query[DBOutput].to[List]

  def getTransactionById(transactionId: String): doobie.ConnectionIO[Option[DBTransaction]] =
    sql"SELECT * FROM transactions WHERE id = $transactionId".query[DBTransaction].to[List].map(_.headOption)

  def getOutput(id: String): doobie.ConnectionIO[Option[DBOutput]] =
    sql"SELECT * FROM outputs WHERE id = $id".query[DBOutput].to[List].map(_.headOption)

  def getContract(transactionId: String): doobie.ConnectionIO[List[Contract]] =
    sql"select * from contracts where hash IN (select contract from inputs where txId = $transactionId)".query[Contract].to[List]
}