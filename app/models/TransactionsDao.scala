package models

import javax.inject.Inject
import models.database.DBService
import models.database.TransactionsQueries._
import scala.concurrent.{ExecutionContext, Future}

class TransactionsDao @Inject()(dBService: DBService)(implicit ec: ExecutionContext) {

  def transactionsByBlock(id: String): Future[List[Transaction]] = {
    val a = dBService.runAsync(getTransactionsByBlockId(id))
    println(a)
    a
  }

  def inputsByTransaction(id: String): Future[List[Input]] = dBService.runAsync(getTransactionInputs(id))

  def outputsByTransaction(id: String): Future[List[Output]] = dBService.runAsync(getTransactionOutputs(id))

  def transactionById(id: String): Future[Option[Transaction]] = dBService.runAsync(getTransactionById(id))
}
