package controllers

import com.typesafe.scalalogging.StrictLogging
import javax.inject.Inject
import models._
import play.api.libs.circe.Circe

import scala.concurrent.ExecutionContext
import io.circe.generic.auto._
import io.circe.syntax._
import org.encryfoundation.common.transaction.{EncryAddress, Pay2ContractHashAddress, Pay2PubKeyAddress, PubKeyLockedContract}
import play.api.mvc._
import scorex.crypto.encode.Base16

class WalletController @Inject()(cc: ControllerComponents,
                                 boxesDao: BoxesDao)
                                (implicit ex: ExecutionContext) extends AbstractController(cc) with ControllerHelpers with Circe with StrictLogging {

  def info(contractHash: String, from: Int, to: Int): Action[AnyContent] = Action.async {
    boxesDao.getBoxesByContractHash(contractHash, from: Int, to: Int).map {
      case Nil => NotFound
      case list: List[Output] => Ok(list.asJson)
    }
  }


  def getWalletByAddress(contractHash: String): Action[AnyContent] = Action.async{
    boxesDao.getWalletByHash(contractHashByAddress(contractHash)).map{
      case Nil => NotFound
      case list: List[Wallet]  => Ok(views.html.wallet(list))
    }
  }

  private def contractHashByAddress(address: String): String = EncryAddress.resolveAddress(address).map {
    case p2pk: Pay2PubKeyAddress => PubKeyLockedContract(p2pk.pubKey).contractHashHex
    case p2sh: Pay2ContractHashAddress => Base16.encode(p2sh.contractHash)
  }.getOrElse(throw EncryAddress.InvalidAddressException)


}