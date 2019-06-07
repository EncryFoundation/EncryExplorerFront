package settings

import org.encryfoundation.common.Algos

object Utils {

  val IntrinsicTokenId: Array[Byte] = Algos.hash("intrinsic_token")

  val EttTokenId: String = Algos.encode(IntrinsicTokenId)
}
