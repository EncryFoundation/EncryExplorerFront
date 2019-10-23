package models.dao

import javax.inject.Inject
import models.{Contract, DBInput, DBOutput, DBTransaction, FullFilledTransaction}
import models.database.TransactionsQueries._
import models.service.{DBService, TransService}

import scala.concurrent.{ExecutionContext, Future}

class TransactionsDao @Inject()(dbService: DBService, transService: TransService)(implicit ec: ExecutionContext) {

  def transactionsByBlock(id: String): Future[List[DBTransaction]] = dbService.runAsync(getTransactionsByBlockId(id))

  def inputsByTransaction(id: String): Future[List[DBInput]] = dbService.runAsync(getTransactionInputs(id))

  def outputsByTransaction(id: String): Future[List[DBOutput]] = dbService.runAsync(getTransactionOutputs(id))

  def transactionById(id: String): Future[Option[DBTransaction]] = {
    transService.getTransactionById(id).flatMap { txOpt =>
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
    transService.getFullTransactionById(id).flatMap { txOpt =>
      if(txOpt.isEmpty) getFullTransaction(id)
      else Future(txOpt)
    }
  }

  def outputById(id: String): Future[Option[DBOutput]] = dbService.runAsync(getOutput(id))

  def contractByTransaction(id: String): Future[List[Contract]] = dbService.runAsync(getContract(id))

  def unconfirmedTransactions(from: Int, to: Int): Future[List[DBTransaction]] = transService.getUnconfirmedTransactions(from, to)
}
