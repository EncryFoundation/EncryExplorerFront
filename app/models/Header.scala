package models

case class Header(id: String,
                  version: Int,
                  parentId: String,
                  transactionsRoot: String,
                  timestamp: Long,
                  height: Int,
                  nonce: Long,
                  difficulty: Long,
                  stateRoot: String,
                  equihashSolution: String,
                  txCount: Int,
                  minerAddress: String,
                  minerReward: Long)
