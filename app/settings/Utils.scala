package settings

import org.encryfoundation.common.Algos
import org.encryfoundation.common.transaction.{EncryAddress, Pay2ContractHashAddress, Pay2PubKeyAddress, PubKeyLockedContract}
import scorex.crypto.encode.Base16

object Utils {

  val IntrinsicTokenId: Array[Byte] = Algos.hash("intrinsic_token")

  val ETTid: String = Algos.encode(IntrinsicTokenId)

  def contractHashByAddress(address: String): String = EncryAddress.resolveAddress(address).map {
    case p2pk: Pay2PubKeyAddress => PubKeyLockedContract(p2pk.pubKey).contractHashHex
    case p2sh: Pay2ContractHashAddress => Base16.encode(p2sh.contractHash)
  }.getOrElse(throw EncryAddress.InvalidAddressException)
}
