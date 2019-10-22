package models.dao

import javax.inject.Inject
import models.{DBTransaction, DBOutput, Wallet}
import models.database.{BoxesQueries, DBService, WalletQueries}

import scala.concurrent.{ExecutionContext, Future}

class BoxesDao @Inject()(dBService: DBService)(implicit ec: ExecutionContext){

  def getBoxesByContractHash(contractHash: String, from: Int, to: Int): Future[List[DBOutput]] =
    dBService.runAsync(BoxesQueries.getBoxesByContractHash(contractHash, from, to))

  def getWalletByHash(contractHash: String): Future[List[Wallet]] =
    dBService.runAsync(WalletQueries.getWallet(contractHash))

  def getTxsIdByHash(contractHash: String): Future[List[String]] =
    dBService.runAsync(WalletQueries.getTxsIdByHash(contractHash))

  def getLastTxById(Id: String): Future[DBTransaction] =
    dBService.runAsync(WalletQueries.getLastTxsByHash(Id))

}
