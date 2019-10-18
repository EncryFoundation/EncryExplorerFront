package models

import javax.inject.Inject
import models.database.{CacheService, DBService}
import models.database.TransactionsQueries._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class TransactionsDao @Inject()(dbService: DBService, cacheService: CacheService)(implicit ec: ExecutionContext) {

  def transactionsByBlock(id: String): Future[List[DBTransaction]] = dbService.runAsync(getTransactionsByBlockId(id))

  def inputsByTransaction(id: String): Future[List[DBInput]] = dbService.runAsync(getTransactionInputs(id))

  def outputsByTransaction(id: String): Future[List[Output]] = dbService.runAsync(getTransactionOutputs(id))

  def transactionById(id: String): Future[Option[DBTransaction]] = {
    cacheService.getTransactionById(id) andThen {
      case Success(None) => dbService.runAsync(getTransactionById(id))
    }
  }

  def outputById(id: String): Future[Option[Output]] = dbService.runAsync(getOutput(id))

  def contractByTransaction(id: String): Future[List[Contract]] = dbService.runAsync(getContract(id))

  def uncommittedTransactions(): Future[List[DBTransaction]] = cacheService.getUncommittedTransactions
}
