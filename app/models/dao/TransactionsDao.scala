package models.dao

import javax.inject.Inject
import models.database.TransactionsQueries._
import models.database.{CacheService, DBService}
import models._

import scala.concurrent.{ExecutionContext, Future}

class TransactionsDao @Inject()(dbService: DBService, cacheService: CacheService)(implicit ec: ExecutionContext) {

  def transactionsByBlock(id: String): Future[List[DBTransaction]] = dbService.runAsync(getTransactionsByBlockId(id))

  def inputsByTransaction(id: String): Future[List[DBInput]] = dbService.runAsync(getTransactionInputs(id))

  def outputsByTransaction(id: String): Future[List[DBOutput]] = dbService.runAsync(getTransactionOutputs(id))

  def transactionById(id: String): Future[Option[DBTransaction]] = {
    cacheService.getTransactionById(id).flatMap { txOpt =>
      if(txOpt.isEmpty) dbService.runAsync(getTransactionById(id))
      else Future(txOpt)
    }
  }

  private def getFullTransaction(id: String): Future[Option[FullFilledTransaction]] =
    transactionById(id).flatMap {
      case Some(tx) =>
        val outputsF: Future[List[DBOutput]] = outputsByTransaction(tx.id)
        val inputsF: Future[List[DBInput]] = inputsByTransaction(tx.id)
        val contractF: Future[List[Contract]] = contractByTransaction(tx.id)
        for {
          outputs <- outputsF
          inputs <- inputsF
          contract <- contractF
        } yield Some(FullFilledTransaction(tx, inputs, outputs, contract))

      case _ => Future(Option.empty[FullFilledTransaction])
    }

  def fullTransactionById(id: String): Future[Option[FullFilledTransaction]] = {
    cacheService.getFullTransactionById(id).flatMap { txOpt =>
      if(txOpt.isEmpty) getFullTransaction(id)
      else Future(txOpt)
    }
  }

  def outputById(id: String): Future[Option[DBOutput]] = dbService.runAsync(getOutput(id))

  def contractByTransaction(id: String): Future[List[Contract]] = dbService.runAsync(getContract(id))

  def unconfirmedTransactions(from: Int, to: Int): Future[List[DBTransaction]] = cacheService.getUnconfirmedTransactions(from, to)
}