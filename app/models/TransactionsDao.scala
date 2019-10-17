package models

import javax.inject.Inject
import models.database.{DBService, DataService}
import models.database.TransactionsQueries._

import scala.concurrent.{ExecutionContext, Future}

class TransactionsDao @Inject()(dBService: DBService, dataService: DataService)(implicit ec: ExecutionContext) {

  def transactionsByBlock(id: String): Future[List[DBTransaction]] = dBService.runAsync(getTransactionsByBlockId(id))

  def inputsByTransaction(id: String): Future[List[Input]] = dBService.runAsync(getTransactionInputs(id))

  def outputsByTransaction(id: String): Future[List[Output]] = dBService.runAsync(getTransactionOutputs(id))

  def transactionById(id: String): Future[Option[DBTransaction]] = dBService.runAsync(getTransactionById(id))

  def outputById(id: String): Future[Option[Output]] = dBService.runAsync(getOutput(id))

  def contractByTransaction(id: String): Future[List[Contract]] = dBService.runAsync(getContract(id))

  def uncommittedTransactions(): Future[List[DBTransaction]] = dataService.getUncommittedTransactions
}
