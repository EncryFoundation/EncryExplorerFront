package models

case class Block(header: Header, payload: List[DBTransaction])