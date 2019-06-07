package models

import javax.inject.Inject
import models.database.{BoxesQueries, DBService, WalletQueries}

import scala.concurrent.{ExecutionContext, Future}

class BoxesDao @Inject()(dBService: DBService)(implicit ec: ExecutionContext){

  def getBoxesByContractHash(contractHash: String, from: Int, to: Int): Future[List[Output]] =
    dBService.runAsync(BoxesQueries.getBoxesByContractHash(contractHash, from, to))

  def getWalletByHash(contractHash: String): Future[List[Wallet]] =
    dBService.runAsync(WalletQueries.getWallet(contractHash))
}
