package models

case class Header(id: String,
                  version: Int,
                  parentId: String,
                  transactionsRoot: String,
                  timestamp: Long,
                  height: Int,
                  nonce: Long,
                  difficulty: Long,
                  equihashSolution: String,
                  txCount: Int,
                  minerAddress: String,
                  minerReward: Long)
