package settings

import org.encryfoundation.common.modifiers.mempool.transaction.{EncryAddress, Pay2ContractHashAddress, Pay2PubKeyAddress, PubKeyLockedContract}
import org.encryfoundation.common.utils.Algos
import scorex.crypto.encode.Base16

object Utils {

  val IntrinsicTokenId: Array[Byte] = Algos.hash("intrinsic_token")

  val EttTokenId: String = Algos.encode(IntrinsicTokenId)

  def contractHashByAddress(address: String): String = EncryAddress.resolveAddress(address).map {
    case p2pk: Pay2PubKeyAddress => PubKeyLockedContract(p2pk.pubKey).contractHashHex
    case p2sh: Pay2ContractHashAddress => Base16.encode(p2sh.contractHash)
  }.getOrElse(throw EncryAddress.InvalidAddressException)
}
