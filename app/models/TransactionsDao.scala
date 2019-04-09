package models

import javax.inject.Inject
import models.database.DBService
import models.database.TransactionsQueries._
import models.database.InputQueries._
import models.database.OutputQueries._
import scala.concurrent.{ExecutionContext, Future}

class TransactionsDao @Inject()(dBService: DBService)(implicit ec: ExecutionContext) {

  def transactionsByBlock(id: String): Future[List[Transaction]] = dBService.runAsync(getTransactionsByBlockId(id))

  def inputsByTransaction(id: String): Future[List[Input]] = dBService.runAsync(getTransactionInputs(id))

  def outputsByTransaction(id: String): Future[List[Output]] = dBService.runAsync(getTransactionOutputs(id))

  def directivesByTransaction(id: String): Future[List[Directive]] = dBService.runAsync(getTransactionDirectives(id))

  def transactionById(id: String): Future[Option[Transaction]] = dBService.runAsync(getTransactionById(id))

  def findOutput(id: String): Future[Option[Output]] = dBService.runAsync(findOutputQuery(id))

  def findOutputsByTxId(id: String): Future[List[Output]] = dBService.runAsync(findOutputsByTxIdQuery(id))

  def findInput(id: String): Future[Option[Input]] = dBService.runAsync(findInputQuery(id))

  def listInputs(txId: String): Future[List[Input]] = dBService.runAsync(listInputsByTxIdQuery(txId))

  def findTransaction(id: String): Future[Option[Transaction]] = dBService.runAsync(findTransactionQuery(id))

  def findTransactionByBlockHeightRange(from: Int, to: Int): Future[List[Transaction]] = dBService.runAsync(findTransactionByBlockHeightRangeQuery(from, to))

  def findOutputByBlockId(id: String): Future[List[Output]] = dBService.runAsync(findOutputByBlockIdQuery(id))
}
