package controllers

import com.typesafe.scalalogging.StrictLogging
import javax.inject.Inject
import models._
import play.api.libs.circe.Circe

import scala.concurrent._
import io.circe.generic.auto._
import io.circe.syntax._
import models.dao.{BoxesDao, TransactionsDao}
import play.api.mvc._
import settings.Utils

class WalletController @Inject()(cc: ControllerComponents,
                                 boxesDao: BoxesDao,
                                 transactionsDao: TransactionsDao)
                                (implicit ex: ExecutionContext) extends AbstractController(cc) with ControllerHelpers with Circe with StrictLogging {

  def info(contractHash: String, from: Int, to: Int): Action[AnyContent] = Action.async {
    boxesDao.getBoxesByContractHash(contractHash, from: Int, to: Int).map {
      case Nil => NotFound
      case list: List[Output] => Ok(list.asJson)

    }
  }

  def getWalletByAddress(contractHash: String): Action[AnyContent] = Action.async{
      val walletF: Future[List[Wallet]] =  boxesDao.getWalletByHash(Utils.contractHashByAddress(contractHash))
      val txIdF = boxesDao.getTxsIdByHash(Utils.contractHashByAddress(contractHash)).flatMap(x => Future.sequence(x.map(id => boxesDao.getLastTxById(id))))

   val result: Future[(List[Wallet], List[DBTransaction])] = for {
      wallet <- walletF
      txs    <- txIdF
    } yield (wallet, txs)

    result.map {
      case (wallet, txs) => Ok(views.html.wallet(wallet,txs))
      case _ => NotFound
    }
  }
}