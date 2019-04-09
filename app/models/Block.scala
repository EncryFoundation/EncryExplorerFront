package models

case class Block(header: Header, payload: List[Transaction])

case class BlockApi(header: Header, payload: List[FullFilledTransactionApi])